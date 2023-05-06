package x590.jdecompiler.scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.VariableNotFoundException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VariableDefineOperation;
import x590.jdecompiler.operation.store.StoreOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.variable.EmptyableVariable;
import x590.jdecompiler.variable.UnnamedVariable;
import x590.jdecompiler.variable.Variable;
import x590.jdecompiler.variable.VariableWrapper;
import x590.jdecompiler.variable.EmptyableVariableWrapper;
import x590.util.IntHolder;
import x590.util.LoopUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Описывает область видимости (Scope). Почти всё, что в исходном коде
 * оборачивается фигурными скобками, является Scope-ом.
 */
public abstract class Scope extends AbstractOperation {
	
	private final List<Operation> code = new ArrayList<>();
	private final List<Scope> scopes = new ArrayList<>();
	
	private final @Immutable List<Operation> unmodifiableCode = Collections.unmodifiableList(code);
	
	private int startIndex, endIndex; // Невключительно
	private final @Nullable Scope superScope;
	
	/** Список локальных переменных */
	private final List<EmptyableVariableWrapper> locals;

	// key: index, value: index in code
	/* private */ public final Int2IntMap indexTable;
	
	private @Nullable String label;
	private int labelNumber;
	
	
	public Scope(DecompilationContext context, int endIndex) {
		this(context, context.currentIndex(), endIndex);
	}
	
	public Scope(DecompilationContext context, int endIndex, Scope superScope) {
		this(context.currentIndex(), endIndex, superScope);
	}
	
	public Scope(DecompilationContext context, int startIndex, int endIndex) {
		this(startIndex, endIndex, context.currentScope());
	}
	
	public Scope(int startIndex, int endIndex, Scope superScope) {
		this(startIndex, endIndex, superScope, new ArrayList<>(superScope.locals));
	}
	
	public Scope(int startIndex, int endIndex, @Nullable Scope superScope, List<EmptyableVariableWrapper> locals) {
		assert startIndex <= endIndex;
		assert superScope != this;
		
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.superScope = superScope;
		this.locals = locals;
		this.indexTable = new Int2IntOpenHashMap(endIndex - startIndex);
	}
	
	
	public int startIndex() {
		return startIndex;
	}
	
	public int endIndex() {
		return endIndex;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	
	/** Расширяет scope до указанного индекса, если возможно */
	public boolean expandTo(int newEndIndex) {
		return endIndex == newEndIndex;
	}
	
	public Scope superScope() {
		return superScope;
	}
	
	
	@Override
	public boolean isTerminable() {
		return isLastOperationTerminable();
	}
	
	@Override
	public final boolean canInlineInLambda() {
		return false;
	}
	
	public boolean isLastOperationTerminable() {
		var code = this.code;
		return !code.isEmpty() && code.get(code.size() - 1).isTerminable();
	}
	
	
	protected void addVariable(EmptyableVariable var) {
		locals.add(var.wrapped());
	}
	
	/** Устанавливает переменную {@code var} в слот {@code slot} для текущего scope и для всех вложенных, начиная с {@code fromIndex} */
	private VariableWrapper setVariable(int slot, int fromIndex, Variable var) {
		var varWrapper = locals.get(slot).assign(var);
		locals.set(slot, varWrapper);
		
		scopes.stream()
				.filter(innerScope -> innerScope.isAfterIndex(fromIndex))
				.forEach(innerScope -> innerScope.setVariable(slot, fromIndex, var));
		
		return varWrapper;
	}
	
	
	private boolean isAfterIndex(int index) {
		return endIndex > index;
	}
	
	
	public Variable getDefinedVariable(int slot) {
		EmptyableVariable var = findVariable(slot, 0);
		
		if(var.isNonEmpty())
			return var.nonEmpty();
		
		throw new VariableNotFoundException("Variable #" + slot + " not found in scope " + this.toString());
	}
	
	
	public Variable getVariableOrDefine(int slot, int fromIndex, Type type) {
		EmptyableVariableWrapper var = findVariable(slot, fromIndex);
		
		return var.isNonEmpty() ? var.nonEmpty() :
			setVariable(slot, fromIndex, new UnnamedVariable(this, type));
	}
	
	
	public Variable defineNewVariable(int slot, Type type, int fromIndex) {
		if(!locals.get(slot).isEmpty())
			throw new DecompilationException("Variable #" + slot + " " + locals.get(slot) + " already defined for scope " + this);
		
		return setVariable(slot, fromIndex, new UnnamedVariable(this, type)).defined();
	}
	
	
	/** Ищет переменную в текущем scope и во всех вложенных */
	protected EmptyableVariableWrapper findVariable(int slot, int fromIndex) {
		EmptyableVariableWrapper var = locals.get(slot);
		
		if(!var.isEmpty())
			return var;
		
		Optional<EmptyableVariableWrapper> foundVar = scopes.stream()
				.filter(innerScope -> innerScope.isAfterIndex(fromIndex))
				.map(innerScope -> innerScope.findVariable(slot, fromIndex))
				.filter(EmptyableVariableWrapper::isNonEmpty).findAny();
		
		foundVar.ifPresent(varWrapper -> setVariable(slot, fromIndex, varWrapper.nonEmpty()).setEnclosingScope(this));
		
		return foundVar.orElse(var);
	}
	
	private static Predicate<EmptyableVariable> varNameEquals(String name) {
		return var -> var.hasName() && var.getName().equals(name);
	}
	
	public EmptyableVariable getVariableWithName(String name) {
		return locals.stream().filter(varNameEquals(name)).findAny().map(var -> (EmptyableVariable)var)
				.orElseGet(() -> scopes.stream().map(scope -> scope.getVariableWithName(name))
							.filter(EmptyableVariable::isNonEmpty).findAny().orElse(Variable.empty()));
	}
	
	public boolean hasVariableWithName(String name) {
		return locals.stream().anyMatch(varNameEquals(name)) ||
				scopes.stream().anyMatch(scope -> scope.hasVariableWithName(name));
	}
	
	/** Выполняет сведение типа для всех переменных и всех локальных переменных в этом и во вложенных scope-ах */
	protected void reduceVariablesTypes() {
		locals.forEach(EmptyableVariable::reduceType);
		scopes.forEach(Scope::reduceVariablesTypes);
	}
	
	/** Объявляет все необъявленные переменные в этом и во вложенных scope-ах */
	protected void defineVariables() {
		locals.stream()
			.filter(EmptyableVariable::isNonEmpty).map(EmptyableVariable::nonEmpty)
			.forEach(variable -> {
				if(!variable.isDefined()) {
					
					Optional<StoreOperation> foundStore = code.stream()
							.filter(operation -> operation instanceof StoreOperation store && store.getVariable() == variable)
							.findFirst().map(operation -> (StoreOperation)operation);
					
					if(foundStore.isPresent() && foundStore.get().defineVariable()) {
						return;
					}
					
					code.add(foundStore.isPresent() ? code.indexOf(foundStore.get()) : 0, new VariableDefineOperation(variable));
				}
			});
		
		scopes.forEach(Scope::defineVariables);
	}
	
	
	/** Определяет имена всех переменных в этом и во вложенных scope-ах */
	protected void assignVariablesNames() {
		locals.forEach(EmptyableVariable::assignName);
		scopes.forEach(Scope::assignVariablesNames);
	}
	
	
	public @Immutable List<Operation> getOperations() {
		return unmodifiableCode;
	}
	
	public int getOperationsCount() {
		return code.size();
	}
	
	public Operation getOperationAt(int index) {
		return code.get(index);
	}
	
	
	public boolean isEmpty() {
		return code.isEmpty();
	}
	
	
	public @Nullable Operation getLastOperation() {
		var code = this.code;
		return code.isEmpty() ? null : code.get(code.size() - 1);
	}
	
	public void addOperation(Operation operation, DecompilationContext context) {
		addOperation(operation, operation.getAddingIndex(context));
	}
	
	public void addOperation(Operation operation, int fromIndex) {
		indexTable.put(fromIndex, code.size());
		
		code.add(operation);
		
		if(operation instanceof Scope scope)
			scopes.add(scope);
	}
	
	public void addOperations(List<? extends Operation> operations, int fromIndex) {
		for(Operation operation : operations)
			addOperation(operation, fromIndex);
	}
	
	public void addOperationsFrom(Scope other) {
		
		List<Operation> operations = other.getOperations();
		
		for(int i = 0, size = operations.size(); i < size; i++) {
			addOperation(operations.get(i), other.getIndexByCodeIndex(i));
		}
	}
	
	
	private static final Comparator<Entry> keyComparator = (entry1, entry2) -> entry1.getIntKey() - entry2.getIntKey();
	
	/* protected */public int getCodeIndexByIndex(int index) {
		return indexTable.int2IntEntrySet().stream()
				.filter(entry -> entry.getIntKey() >= index)
				.min(keyComparator)
				.map(Entry::getIntValue)
				.orElseGet(() ->
						indexTable.int2IntEntrySet().stream()
								.max(keyComparator)
								.map(Entry::getIntValue)
								.orElse(-1) + 1
				);
		
//		Old code
//		int foundIndex = indexTable.getOrDefault(index, NONE_INDEX);
//		
//		if(foundIndex != NONE_INDEX)
//			return foundIndex;
//		
//		final int srcIndex = index;
//		final int maxIndex = indexTable.keySet().intStream().max().orElse(0);
//		
//		if(index == maxIndex + 1) {
//			return indexTable.get(maxIndex) + 1;
//		}
//		
//		while(foundIndex == NONE_INDEX && index < maxIndex) {
//			foundIndex = indexTable.getOrDefault(++index, NONE_INDEX);
//		}
//		
//		if(foundIndex != NONE_INDEX) {
//			return foundIndex;
//		}
//		
//		throw new DecompilationException(new NoSuchElementException(Integer.toString(srcIndex)));
	}
	
	protected int getIndexByCodeIndex(int codeIndex) {
		return indexTable.int2IntEntrySet().stream()
				.filter(entry -> entry.getIntValue() == codeIndex).findAny()
				.orElseThrow(() -> new NoSuchElementException(Integer.toString(codeIndex)))
				.getIntKey();
	}
	
	
	/** Возвращает неизменяемый список операций, начиная с заданного индекса.
	 * 
	 * @param fromIndex - индекс, с которого будут возвращаться операции.
	 * Преобразуется в индекс для внутреннего массива.
	 * Не должен быть меньше {@link #startIndex} или больше {@link #endIndex}.
	 * 
	 * @implNote Может изменить внутреннее состояние при добавлении и удалении операций в scope,
	 * в том числе и при вызове метода {@link #pullOperationsFromIndex(int)}. */
	public @Immutable List<Operation> getOperationsFromIndex(int fromIndex) {
		
		assert fromIndex >= startIndex && fromIndex <= endIndex :
			"startIndex = " + startIndex + ", endIndex = " + endIndex + ", fromIndex = " + fromIndex;
		
		return Collections.unmodifiableList(code.subList(getCodeIndexByIndex(fromIndex), code.size()));
	}
	
	
	/** Удаляет все операции, начиная с заданного индекса, и возвращает их.
	 * 
	 * @param fromIndex - индекс операции в коде (тот индекс, который возвращается,
	 * например, методом {@link DecompilationContext#currentIndex()}).
	 * {@code fromIndex} преобразуется в индекс для внутреннего массива.
	 * Не должен быть меньше {@link #startIndex} или больше {@link #endIndex}. */
	public List<Operation> pullOperationsFromIndex(int fromIndex) {
		
		assert fromIndex >= startIndex && fromIndex <= endIndex :
			"startIndex = " + startIndex + ", endIndex = " + endIndex + ", fromIndex = " + fromIndex;
		
		int indexInCode = getCodeIndexByIndex(fromIndex);
		
		// Метод List.subList(int, int) не копирует внутренний массив,
		// поэтому мы можем удалить все элементы, начиная с indexInCode, просто очистив sublist
		List<Operation> sublist = code.subList(indexInCode, code.size());
		List<Operation> sublistCopy = new ArrayList<>(sublist);
		sublist.clear();
		
		scopes.removeAll(sublistCopy);
		
		indexTable.int2IntEntrySet().removeIf(entry -> entry.getIntValue() >= indexInCode);
		
		return sublistCopy;
	}
	
	public void removeOnlySelf() {
		super.remove();
	}
	
	@Override
	public void remove() {
		super.remove();
		code.forEach(operation -> {
				if(operation instanceof Scope scope)
					scope.removeIfInBounds(startIndex, endIndex);
				else
					operation.remove();
			});
	}
	
	
	private void removeIfInBounds(int startIndex, int endIndex) {
		if(this.startIndex >= startIndex && this.endIndex <= endIndex) {
			this.remove();
		}
	}
	
	
	/** Удаляет все операции, отмеченные флагом {@link Operation#isRemovedFromScope()} или {@link Operation#canOmit()}.
	 * При этом обновляется {@link #indexTable}, так как индексы могут сместиться */
	protected void deleteRemovedOperations() {
		IntHolder indexHolder = new IntHolder();
		
		code.removeIf(operation -> {
			
			if(operation.isRemovedFromScope() || operation.canOmit()) {
				int indexInCode = indexHolder.get();
				
				indexTable.int2IntEntrySet().removeIf(entry -> entry.getIntKey() == indexInCode);
				
				indexTable.int2IntEntrySet().stream()
						.filter(entry -> entry.getIntValue() > indexInCode)
						.forEach(entry -> entry.setValue(entry.getIntValue() - 1));
				
				return true;
			}
			
			indexHolder.preInc();
			
			return false;
		});
	}
	
	
	/** Можно ли использовать {@literal break} для этого scope */
	public boolean isBreakable() {
		return false;
	}
	
	/** Можно ли использовать {@literal continue} для этого scope */
	public boolean isContinuable() {
		return false;
	}
	
	
	public void initLabel() {
		if(label != null) {
			return;
		}
		
		label = getLabelBaseName();
		
		int count = superScope.getScopesCountWithLabel(label);
		
		labelNumber = count == 0 ? count : count + 1;
	}
	
	public void writeLabel(StringifyOutputStream out) {
		if(label == null)
			throw new IllegalStateException("Label not initialized");
		
		out.write(label);
		
		if(labelNumber != 0)
			out.write(Integer.toString(labelNumber));
	}
	
	protected String getLabelBaseName() {
		return "L";
	}
	
	private int getScopesCountWithLabel(String labelName) {
		int count = 0;
		
		if(labelName.equals(label)) {
			count++;
			
			if(labelNumber == 0)
				labelNumber = 1;
		}
		
		for(Scope scope : scopes) {
			count += scope.getScopesCountWithLabel(labelName);
		}
		
		if(superScope != null)
			return count + superScope.getScopesCountWithLabel(labelName);
		
		return count;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeHeader(out, context);
		
		if(code.isEmpty()) {
			if(canOmitCurlyBrackets())
				out.write(';');
			else
				out.write(" {}");
			
		} else {
			
			boolean canOmitCurlyBrackets = canOmitCurlyBrackets();
			
			if(!canOmitCurlyBrackets)
				out.write(" {");
			
			writeBody(out, context);
			
			if(!canOmitCurlyBrackets)
				out.println().printIndent().print('}');
		}
	}
	
	protected void writeBody(StringifyOutputStream out, StringifyContext context) {
		out.increaseIndent();
		
		LoopUtil.forEachPair(code,
				operation -> operation.writeAsStatement(out, context),
				(operation1, operation2) -> operation1.writeSeparator(out, context, operation2));
		
		out.reduceIndent();
	}
	
	/**
	 * Можно ли записать точку с запятой вместо фигурных скобок
	 * (когда scope пустой или в нём только одна операция)
	 */
	protected boolean canOmitCurlyBrackets() {
		return JDecompiler.getConfig().canOmitCurlyBrackets() &&
				(code.isEmpty() || code.size() == 1 && (
						code.get(0) instanceof Scope scope ?
								scope.canOmitCurlyBrackets() :
								!code.get(0).isVariableDefinition()
				));
	}
	
	
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {}
	
	@Override
	public void writeBack(StringifyOutputStream out, StringifyContext context) {}
	
	@Override
	public void writeSeparator(StringifyOutputStream out, StringifyContext context, Operation nextOperation) {
		out.println();
	}
	
	
	/** Выполняется перед тем, как scope будет завершён и убран со стека scope-ов.
	 * По умолчанию вызывает метод {@link #deleteRemovedOperations()} */
	public void finalizeScope(DecompilationContext context) {
		deleteRemovedOperations();
	}
	
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s {%d - %d}",
				getClass().getSimpleName(), startIndex, endIndex);
	}
	
	public void remove(Operation operation) {
		code.remove(operation);
		if(operation.isScope())
			scopes.remove(operation);
	}
	
	@Override
	public final boolean equals(Operation other) {
		return this == other;
	}
}
