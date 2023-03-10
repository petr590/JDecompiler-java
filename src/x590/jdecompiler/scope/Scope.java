package x590.jdecompiler.scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.VariableNotFoundException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VariableDefineOperation;
import x590.jdecompiler.operation.store.StoreOperation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.variable.EmptyableVariable;
import x590.jdecompiler.variable.UnnamedVariable;
import x590.jdecompiler.variable.Variable;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Описывает область видимости (Scope). Почти всё, что в исходном коде
 * оборачивается фигурными скобками, является Scope-ом.
 */
public abstract class Scope extends Operation {
	
	private final List<Operation> code = new ArrayList<>();
	private final List<Scope> scopes = new ArrayList<>();
	
	private final @Immutable List<Operation> unmodifiableCode = Collections.unmodifiableList(code);
	
	private int startIndex, endIndex; // Невключительно
	private final @Nullable Scope superScope;
	
	private final List<EmptyableVariable> locals;
	private final Int2IntMap indexTable;
	
	
	public Scope(DecompilationContext context, int endIndex) {
		this(context, context.currentIndex(), endIndex);
	}
	
	public Scope(DecompilationContext context, int endIndex, @Nullable Scope superScope) {
		this(context.currentIndex(), endIndex, superScope);
	}
	
	public Scope(DecompilationContext context, int startIndex, int endIndex) {
		this(startIndex, endIndex, context.currentScope());
	}
	
	public Scope(int startIndex, int endIndex, @Nullable Scope superScope) {
		this(startIndex, endIndex, superScope, new ArrayList<>(superScope.locals));
	}
	
	public Scope(int startIndex, int endIndex, @Nullable Scope superScope, List<EmptyableVariable> locals) {
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
	
	public Scope superScope() {
		return superScope;
	}
	
	
	protected void addLocalVariable(EmptyableVariable var) {
		locals.add(var);
	}
	
	/** Устанавливает переменную по индексу для текущего scope и для всех вложенных */
	private void setVariable(int index, EmptyableVariable var) {
		locals.set(index, var);
		scopes.forEach(innerScope -> innerScope.setVariable(index, var));
	}
	
	
	public Variable getDefinedVariable(int index) {
		EmptyableVariable var = findVariable(index);
		
		if(!var.isEmpty())
			return var.notEmpty();
		
		throw new VariableNotFoundException("Variable #" + index + " not found in scope " + this.toString());
	}
	
	
	public Variable getVariableOrDefine(int index, Type type) {
		EmptyableVariable var = findVariable(index);
		
		if(!var.isEmpty())
			return var.notEmpty();
		
		var = new UnnamedVariable(this, type);
		setVariable(index, var);
		
		return var.notEmpty();
	}
	
	/** Ищет переменную в текущем scope и во всех вложенных */
	protected EmptyableVariable findVariable(int index) {
		EmptyableVariable var = locals.get(index);
		
		if(!var.isEmpty())
			return var;
		
		for(Scope innerScope : scopes) {
			var = innerScope.findVariable(index);
			if(!var.isEmpty()) {
				Variable notEmptyVar = var.notEmpty();
				
				setVariable(index, notEmptyVar);
				notEmptyVar.setEnclosingScope(this);
				return notEmptyVar;
			}
		}
		
		return Variable.empty();
	}
	
	
	private static Predicate<EmptyableVariable> varNameEquals(String name) {
		return var -> var.hasName() && var.getName().equals(name);
	}
	
	public EmptyableVariable getVariableWithName(String name) {
		return locals.stream().filter(varNameEquals(name)).findAny()
				.orElseGet(() -> {
					for(Scope scope : scopes) {
						EmptyableVariable var = scope.getVariableWithName(name);
						if(!var.isEmpty())
							return var;
					}
					
					return Variable.empty();
				});
	}
	
	public boolean hasVariableWithName(String name) {
		return locals.stream().anyMatch(varNameEquals(name)) ||
				scopes.stream().anyMatch(scope -> scope.hasVariableWithName(name));
	}
	
	/** Выполняет сведение типа для всех переменных и всех операций в этом и во вложенных scope-ах */
	protected void reduceTypes() {
		locals.forEach(EmptyableVariable::reduceType);
		scopes.forEach(Scope::reduceTypes);
	}
	
	/** Объявляет все необъявленные переменные в этом и во вложенных scope-ах */
	protected void defineVariables() {
		locals.stream().filter(var -> !var.isEmpty()).map(EmptyableVariable::notEmpty).forEach(variable -> {
			if(!variable.isDefined()) {
				
				Optional<StoreOperation> foundStore = code.stream().filter(operation -> operation instanceof StoreOperation store && store.getVariable() == variable)
						.map(operation -> (StoreOperation)operation).findFirst();
				
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
	
	
	public @Immutable List<Operation> getCode() {
		return unmodifiableCode;
	}
	
	
	public boolean isEmpty() {
		return code.isEmpty();
	}
	
	
	public @Nullable Operation getLastOperation(DecompilationContext context) {
		return code.isEmpty() ? null : code.get(code.size() - 1);
	}
	
	public void addOperation(Operation operation, DecompilationContext context) {
		addOperation(operation, context.currentIndex());
	}
	
	public void addOperation(Operation operation, int fromIndex) {
		indexTable.put(fromIndex, code.size());
		
		code.add(operation);
		
		if(operation instanceof Scope scope)
			scopes.add(scope);
	}
	
	public void addOperations(List<Operation> operations, int fromIndex) {
		for(Operation operation : operations)
			addOperation(operation, fromIndex);
	}
	
	
	private static final int NONE_INDEX = -1;
	
	protected int getCodeIndexByIndex(int index) {
		int foundIndex = indexTable.getOrDefault(index, NONE_INDEX);
		
		if(foundIndex != NONE_INDEX)
			return foundIndex;
		
		int srcIndex = index;
		int maxIndex = indexTable.keySet().intStream().max().orElse(0);
		
		while(foundIndex == NONE_INDEX && index < maxIndex) {
			foundIndex = indexTable.getOrDefault(++index, NONE_INDEX);
		}
		
		if(foundIndex != NONE_INDEX)
			return foundIndex;
		
		throw new NoSuchElementException(Integer.toString(srcIndex));
	}
	
	protected int getIndexByCodeIndex(int codeIndex) {
		return indexTable.int2IntEntrySet().stream().filter(entry -> entry.getIntValue() == codeIndex)
				.findAny().orElseThrow(() -> new NoSuchElementException(Integer.toString(codeIndex))).getIntKey();
	}
	
	
	/** Возвращает неизменяемый список операций, начиная с заданного индекса.
	 * @param fromIndex - индекс, с которого будут возвращаться операции.
	 * @implNote Может изменить внутреннее состояние при добавлении и удалении операций в scope,
	 * в том числе и при вызове метода {@link #pullOperationsFromIndex(int)} */
	public @Immutable List<Operation> getOperationsFromIndex(int fromIndex) {
		
		assert fromIndex >= startIndex && fromIndex <= endIndex :
			"startIndex = " + startIndex + ", endIndex = " + endIndex + ", fromIndex = " + fromIndex;
		
		return Collections.unmodifiableList(code.subList(getCodeIndexByIndex(fromIndex), code.size()));
	}
	
	
	/** Удаляет все операции, начиная с заданного индекса, и возвращает их.
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
		
		indexTable.int2IntEntrySet().removeIf(entry -> entry.getIntKey() >= indexInCode);
		
		return sublistCopy;
	}
	
	
	public @Immutable List<Operation> getOperations() {
		return Collections.unmodifiableList(code);
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
	
	
	/** Удаляет все операции с установленным флагом {@link Operation#removed} */
	protected void deleteRemovedOperations() {
		code.removeIf(operation -> operation.isRemoved() || operation.canOmit());
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
		out .increaseIndent()
			.printAllUsingFunction(code,
				operation -> operation.writeAsStatement(out, context),
				operation -> operation.writeSeparator(out, context))
			.reduceIndent();
	}
	
	/**
	 * Можно ли записать точку с запятой вместо фигурных скобок
	 * (когда scope пустой или в нём только одна операция)
	 */
	protected boolean canOmitCurlyBrackets() {
		return JDecompiler.getInstance().canOmitCurlyBrackets() &&
				(code.isEmpty() || code.size() == 1 && (!(code.get(0) instanceof Scope scope) || (scope.canOmitCurlyBrackets())));
	}
	
	
	@Override
	public void writeFront(StringifyOutputStream out, StringifyContext context) {
		out.println().printIndent();
	}
	
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {}
	
	@Override
	public void writeBack(StringifyOutputStream out, StringifyContext context) {}
	
	@Override
	public void writeSeparator(StringifyOutputStream out, StringifyContext context) {
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
		return this.getClass().getSimpleName() + " {" + startIndex + ", " + endIndex + "}";
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
