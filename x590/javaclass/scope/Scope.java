package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.VariableNotFoundException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.variable.Variable;
import x590.javaclass.variable.UnnamedVariable;
import x590.javaclass.variable.EmptyableVariable;
import x590.jdecompiler.JDecompiler;

public class Scope extends Operation {
	
	private List<Operation> code = new ArrayList<>();
	private List<Scope> scopes = new ArrayList<>();
	
	protected int startIndex, endIndex;
	
	protected final List<EmptyableVariable> locals;
	
	
	protected Scope(int startIndex, int endIndex, List<EmptyableVariable> locals) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.locals = locals;
	}
	
	public Scope(DecompilationContext context, int endIndex) {
		this(context, context.currentIndex(), endIndex);
	}
	
	public Scope(DecompilationContext context, int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.locals = List.copyOf(context.currentScope().locals);
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
	
	/** Устанавливает переменную по индексу для текущего скопа и для всех вложенных */
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
	
	/** Ищет переменную в текущем скопе и во всех вложенных */
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
	
	/** Выполняет сведение типа для всех переменных и всех операций в этом и во вложенных скопах */
	protected void reduceTypes() {
		locals.forEach(EmptyableVariable::reduceType);
		scopes.forEach(Scope::reduceTypes);
	}
	
	/** Определяет имена всех переменных в этом и во вложенных скопах */
	protected void assignVariablesNames() {
		locals.forEach(EmptyableVariable::assignName);
		scopes.forEach(Scope::assignVariablesNames);
	}
	
	
	public boolean isEmpty() {
		return code.isEmpty();
	}
	
	
	public @Nullable Operation getLastOperation(DecompilationContext context) {
		return code.isEmpty() ? null : code.get(code.size() - 1);
	}
	
	public void addOperation(DecompilationContext context, Operation operation) {
		code.add(operation);
		if(operation.isScope())
			scopes.add((Scope)operation);
	}
	
	
	public void deleteRemovedOperations() {
		code = code.stream().filter(operation -> !operation.isRemoved() && !operation.canOmit()).toList();
		scopes.forEach(Scope::deleteRemovedOperations);
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
			boolean canOmitCurlyBraces = canOmitCurlyBrackets();
			
			if(!canOmitCurlyBraces)
				out.write(" {");
			
			out.increaseIndent();
			code.forEach(operation -> operation.printBack(operation.printFront(out, context).print(operation, context), context));
			out.reduceIndent();
			
			if(!canOmitCurlyBraces)
				out.println().printIndent().print('}');
		}
	}
	
	/** 
	 * Можно ли записать точку с запятой вместо фигурных скобок
	 * когда скоп пустой или в нём только одна операция
	 */
	protected boolean canOmitCurlyBrackets() {
		return code.size() <= 1 && JDecompiler.getInstance().canOmitCurlyBrackets();
	}
	
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {}
	
	@Override
	public StringifyOutputStream printBack(StringifyOutputStream out, StringifyContext context) {
		return out;
	}
	
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
	
	
	@Override
	public final boolean isScope() {
		return true;
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
}