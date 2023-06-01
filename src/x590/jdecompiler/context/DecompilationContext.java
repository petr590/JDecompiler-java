package x590.jdecompiler.context;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import x590.jdecompiler.Importable;
import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable;
import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable.CatchEntry;
import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable.TryEntry;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.method.JavaMethod;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.operator.TernaryOperatorOperation;
import x590.jdecompiler.operation.returning.VReturnOperation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class DecompilationContext extends DecompilationAndStringifyContext implements Importable {
	
	/** Текущий стек метода */
	private final OperationStack stack = new OperationStack();
	
	/** Сохранённые состояния стека по индексам. На один индекс может быть несколько состояний.
	 * {@code Deque<Deque<Operation>>} - список состояний стека на определённом индексе,
	 * {@code Deque<Operation>} - отдельное состояние стека */
	private final Int2ObjectMap<Deque<Deque<Operation>>> stackStates = new Int2ObjectArrayMap<>();
	
	/** Был ли стек обновлён на текущем индексе (т.е. был ли вызван метод {@link #updateStackState()}).
	 * Этот флаг нужен, чтобы не было двойного вызова {@link #updateStackState()} при декомпиляции
	 * тернарного оператора. */
	private boolean stackStateUpdated;
	
	private static final Deque<Deque<Operation>> EMPTY_STACK_STATES = new ArrayDeque<>();
	
	private final @Immutable Set<Operation> operations;
	private final Set<Operation> mutableOperations;
	private Scope currentScope;
	
	private final Queue<Scope> scopesQueue = new LinkedList<>();
	
	/** Для определения индекса начала выражения */
	private final Int2IntMap expressionIndexTable;
	
	/** Точки "разрыва", через которые мы не можем проводить соединение опрераций
	 * (например, инкремент и использование переменной).
	 * Нужно для корректной декомпиляции циклов и, возможно, других конструкций */
	private final IntSet breaks = new IntArraySet();
	
	private Consumer<Operation> nextOperationHandler;
	
	
	public static DecompilationContext decompile(Context otherContext, ClassInfo classinfo, JavaMethod method, List<Instruction> instructions, int maxLocals) {
		return new DecompilationContext(otherContext, classinfo, method, instructions, maxLocals);
	}
	
	private DecompilationContext(Context otherContext, ClassInfo classinfo, JavaMethod method, List<Instruction> instructions, int maxLocals) {
		super(otherContext, classinfo, method);
		
		final var transitionInstructions = new PreDecompilationContext(otherContext, classinfo, method, instructions).getTransitionInstructions();
		
		
		this.currentScope = getMethodScope();
		
		final Set<Operation> operations = new HashSet<>(instructions.size());
		this.operations = Collections.unmodifiableSet(operations);
		this.mutableOperations = operations;
		
		final ExceptionTable exceptionTable = method.getCodeAttribute().getExceptionTable();
		
		final List<TryEntry> tryEntries = exceptionTable.getEntries().stream()
//				.filter(Predicate.not(TryEntry::isFinally))
				.collect(Collectors.toList());
		
		final List<CatchEntry> catchEntries = tryEntries.stream()
				.flatMap(tryEntry -> tryEntry.getCatchEntries().stream())
//				.filter(Predicate.not(CatchEntry::isFinally))
				.collect(Collectors.toList());
		
		final Operation vreturnOperation = VReturnOperation.getInstance();
		
		final var expressionIndexTable = this.expressionIndexTable = new Int2IntArrayMap(instructions.size());
		int expressionIndex = 0;
		
		for(Instruction instruction : instructions) {
			
			final int index = this.index;
			final int currentPos = currentPos();
			
			stackStateUpdated = false;
			updateStackState();
			
			finalizeScopes(index);
			
			
//			Logger.debugf("%d: operation stack: [%s]", index, stack.stream().map(operation -> operation.getClass().getSimpleName() + " " + operation.getReturnType().getName()).collect(Collectors.joining(", ")));
//			Logger.debugf("%d: scope stack: [%s]", index, StreamSupport.stream(this.getCurrentScopes().spliterator(), false).map(Scope::toString).collect(Collectors.joining(", ")));
			
//			Logger.debugf("%d: locals: [%s]", index,
//					Stream.concat(Stream.of(method.getMethodScope()), method.getMethodScope().getOperations().stream()
//							.filter(Operation::isScope)
//							.map(operation -> (Scope)operation))
//							.map(
//									scope -> scope.getClass().getSimpleName() + '[' + scope.locals.stream()
//											.map(var -> var.isEmpty() ? "empty" : "0x" + Integer.toHexString(var.hashCode()))
//											.collect(Collectors.joining(", ")) + ']'
//							
//							).collect(Collectors.joining(", "))
//			);
			
			
			expressionIndexTable.put(index, expressionIndex);
			
			
			tryEntries.removeIf(entry -> entry.getStartPos() == currentPos && addScope(entry.createScope(this)));
			catchEntries.removeIf(entry -> entry.getStartPos() == currentPos && addScope(entry.createScope(this)));
			
			
			transitionInstructions
					.getOrDefault(index, PreDecompilationContext.DEFAULT_TRANSITION_INSTRUCTIONS_LIST)
					.forEach(transitionInstruction -> {
						Operation operation = transitionInstruction.toOperationBeforeTargetIndex(this);
						
						if(operation != null) {
							operations.add(operation);
							
							if(operation instanceof Scope scope) {
								currentScope.addOperation(scope, this);
								scopesQueue.add(scope);
							}
						}
					});
			
			
			startScopes(index);
			
			
			Operation operation;
			
			try {
				operation = instruction.toOperation(this);
			} catch(DecompilationException | EmptyStackException ex) {
				throw new DecompilationException(index, ex);
			} catch(Throwable ex) {
				System.err.println(ex.getClass().getSimpleName() + " at index " + index);
				throw ex;
			}
			
			if(operation != null) {
				operations.add(operation);
				
				if(nextOperationHandler != null) {
					nextOperationHandler.accept(operation);
					nextOperationHandler = null;
				}
				
				if(operation.getReturnType() == PrimitiveType.VOID) {
					expressionIndex = index;
					
					if(operation != vreturnOperation || currentScope != getMethodScope()) {
						currentScope.addOperation(operation, this);
						
						if(operation instanceof Scope scope) {
							scopesQueue.add(scope);
						}
					}
					
					// Мы должны вызывать nextPushHandler только если push вызывается на следующем индексе,
					// но не на последующих. Это нужно для предотвращения багов с декомпиляцией инкремента.
					stack.clearNextPushHandler();
					
				} else {
					push(operation);
				}
			}
			
			
			if(hasBreak(index)) {
				expressionIndex = index;
			}
			
			
			this.index++;
		}
		
		checkScopesBounds(index);
		finalizeScopes(END_INDEX);
		
		assert index == instructions.size() : index + ", " + instructions.size();
		
		index = 0;
		
		instructions.forEach(instruction -> {
			instruction.postDecompilation(this);
			index++;
		});
		
		deleteRemovedOperations();
		
		// Вызывать deduceType пока он возвращает true хотя бы для одной операции
		while(operations.stream().anyMatch(Operation::deduceType));
		
		operations.forEach(Operation::reduceType);
		getMethodScope().reduceVariablesTypes();
		getMethodScope().defineVariables();
		
		postDecompilation();
	}
	
	private void postDecompilation() {
		operations.forEach(operation -> operation.postDecompilation(this));
		deleteRemovedOperations();
	}
	
	public void afterDecompilation() {
		operations.forEach(operation -> operation.afterDecompilation(this));
		deleteRemovedOperations();
	}
	
	
	public void deleteRemovedOperations() {
		mutableOperations.removeIf(Operation::isRemoved);
	}
	
	
	/** Убирает все scope-ы, которые вышли за границу видимости или были удалены.
	 * Затем кладёт на стек все scope-ы, до которых дошла очередь.
	 * Нужен, если мы хотим обновить scope-ы немедленно, не дожидаясь следующего хода */
	public void updateScopes() {
		finalizeScopes(index);
		startScopes(index);
	}
	
	
	private static final int END_INDEX = Integer.MAX_VALUE;
	
	
	/** Убирает все scope-ы, которые вышли за границу видимости или были удалены. */
	private void finalizeScopes(int index) {
		String strIndex = index == END_INDEX ? "End" : Integer.toString(index);
		
		Scope currentScope = this.currentScope;
		
		while(currentScope != null && (currentScope.isRemoved() || index >= currentScope.endIndex())) {
			currentScope.finalizeScope(this);
			
			Logger.logf("%s: %s %s", strIndex, currentScope, currentScope.isRemoved() ? "removed" : "finalized");
			
			this.currentScope = currentScope = currentScope.superScope();
		}
		
	}
	
	/** Кладёт на стек все scope-ы, до которых дошла очередь. */
	private void startScopes(int index) {
		
		for(Iterator<Scope> iter = scopesQueue.iterator(); iter.hasNext(); ) {
			if(startScope(iter.next()))
				iter.remove();
		}
	}
	
	private boolean startScope(Scope scope) {
		
		if(scope.isRemoved()) {
			return true;
			
		} else if(index > scope.startIndex()) {
			
//			assert scope.superScope() == currentScope :
//				"At index " + index + ": " + scope + " must have parent scope " + currentScope + ", not " + scope.superScope();
			
			Logger.logf("%d: %s started", index, scope);
			
			if(index >= scope.endIndex()) {
				scope.finalizeScope(this);
				Logger.logf("%d: %s finalized", index, scope);
				
			} else {
				currentScope = scope;
			}
			
			return true;
		}
		
		return false;
	}
	
	private boolean addScope(Scope scope) {
		if(startScope(scope)) {
			scope.superScope().addOperation(scope, this);
			mutableOperations.add(scope);
			return true;
		}
		
		return false;
	}
	
	private void checkScopesBounds(int lastIndex) {
		for(Scope scope = currentScope; scope != null; scope = scope.superScope()) {
			if(scope.endIndex() > lastIndex) {
				Logger.warningFormatted("Scope %s is out of bounds of the %s", scope, getMethodScope());
			}
		}
	}
	
	
	public OperationStack getStack() {
		return stack;
	}
	
	public Operation pop() {
		return stack.pop();
	}
	
	public Operation popAsNarrowest(Type type) {
		return stack.popAsNarrowest(type);
	}
	
	public Operation popAsWidest(Type type) {
		return stack.popAsWidest(type);
	}
	
	public Operation popWithSize(TypeSize size) {
		return stack.popWithSize(size);
	}
	
	public Operation peek() {
		return stack.peek();
	}
	
	public Operation peek(int index) {
		return stack.peek(index);
	}
	
	public Operation peekAsNarrowest(Type type) {
		return stack.peekAsNarrowest(type);
	}
	
	public Operation peekAsWidest(Type type) {
		return stack.peekAsWidest(type);
	}
	
	public Operation peekWithSize(TypeSize size) {
		return stack.peekWithSize(size);
	}
	
	public Operation peekWithSize(int index, TypeSize size) {
		return stack.peekWithSize(index, size);
	}
	
	public void push(Operation operation) {
		mutableOperations.add(operation);
		stack.push(operation);
	}
	
	public void pushAll(Collection<Operation> operations) {
		stack.pushAll(operations);
		mutableOperations.addAll(operations);
	}
	
	public boolean stackEmpty() {
		return stack.isEmpty();
	}
	
	public int stackSize() {
		return stack.size();
	}
	
	public void onNextPush(Predicate<Operation> nextPushHandler) {
		stack.onNextPush(nextPushHandler);
	}
	
	public void onNextOperationDecompiling(Consumer<Operation> nextOperationHandler) {
		this.nextOperationHandler = nextOperationHandler;
	}
	
	
	/**
	 * Обновляет стек: берёт последнее состояние стека из {@link #stackStates}
	 * и делает его текущим состоянием стека. Также берёт старое состояние стека
	 * и добавляет в начало списка в {@link #stackStates}.<br>
	 * Пример:<br>
	 * было:   state1, [state2, state3]<br>
	 * станет: state3, [state1, state2]
	 */
	public void updateStackState() {
		if(!stackStateUpdated) {
			final var index = this.index;
			final var state = stackStates.getOrDefault(index, EMPTY_STACK_STATES).pollLast();
			
			if(state != null) {
				getOrCreateStackStates(index).addFirst(new ArrayDeque<>(stack.getContent()));
				stack.setState(state);
			}
			
			stackStateUpdated = true;
		}
	}
	
	/** Сбрасывает флаг {@link #stackStateUpdated} */
	public void resetStackStateUpdated() {
		stackStateUpdated = false;
	}
	
	/** Сохраняет по индексу текущее состояние стека */
	public void saveStackState(int index) {
		if(index >= this.index) {
			getOrCreateStackStates(index).addLast(new ArrayDeque<>(stack.getContent()));
		}
	}
	
	private Deque<Deque<Operation>> getOrCreateStackStates(int index) {
		return stackStates.computeIfAbsent(index, key -> new ArrayDeque<>());
	}
	
	
	/** @return Первое состояние стека по индексу или {@literal null}, если его нет */
	public @Nullable Deque<Operation> getStackState(int index) {
		return stackStates.getOrDefault(index, EMPTY_STACK_STATES).peekFirst();
	}
	
	/** Удаляет из списка первое состояние стека по индексу.
	 * @return Удалённое состояние стека или {@literal null}, если удалять нечего. */
	public @Nullable Deque<Operation> pollStackState(int index) {
		return stackStates.getOrDefault(index, EMPTY_STACK_STATES).pollFirst();
	}
	
	
	public Scope currentScope() {
		return currentScope;
	}
	
	public Scope superScope() {
		return currentScope.superScope();
	}
	
	
	public int expressionStartIndexByIndex(int index) {
		return expressionIndexTable.get(index);
	}
	
	public int currentExpressionStartIndex() {
		return expressionIndexTable.get(index);
	}
	
	
	public void addBreak(int index) {
		breaks.add(index);
	}
	
	public boolean hasBreak(int index) {
		return breaks.contains(index);
	}
	
	public boolean hasBreakAtCurrentIndex() {
		return breaks.contains(index);
	}
	
	
	public @Immutable Set<Operation> getOperations() {
		return operations;
	}
	
	public Iterable<Scope> getCurrentScopes() {
		
		return new Iterable<>() {
			
			public Iterator<Scope> iterator() {
				
				return new Iterator<>() {
					
					private Scope scope = currentScope;
					
					public boolean hasNext() {
						return scope != null;
					}
					
					public Scope next() {
						var scope = this.scope;
						this.scope = scope.superScope();
						return scope;
					}
					
				};
			}
		};
	}
	
	
	public void addScopeToQueue(Scope scope) {
		scopesQueue.add(scope);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(operations);
	}
	
	@Override
	public void warning(String message) {
		logWarning(message);
	}
	
	public static void logWarning(String message) {
		Logger.warning("Decompilation warning: " + message);
	}
}
