package x590.javaclass.scope;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import x590.javaclass.UnnamedVariable;
import x590.javaclass.Variable;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.VariableNotFoundException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.jdecompiler.JDecompiler;

public class Scope extends Operation {
	
	private List<Operation> code = new ArrayList<>();
	private List<Scope> scopes = new ArrayList<>();
	
	protected int startIndex, endIndex;
	
	protected final List<Variable> locals;
	
	
	protected Scope(int startIndex, int endIndex, List<Variable> locals) {
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
	
	
	public Variable getDefinedVariable(int index) {
		Variable var = findVariable(index);
		
		if(var != null)
			return var;
		
		throw new VariableNotFoundException("Variable #" + index + " not found in scope " + this.toString());
	}
	
	
	public Variable getVariableOrDefine(int index, Type type) {
		Variable var = findVariable(index);
		
		if(var != null)
			return var;
		
		var = new UnnamedVariable(type);
		locals.set(index, var);
		
		return var;
	}
	
	
	protected @Nullable Variable findVariable(int index) {
		Variable var = locals.get(index);
		
		if(var != null)
			return var;
		
		for(Scope innerScope : scopes) {
			var = innerScope.findVariable(index);
			if(var != null) {
				locals.set(index, var);
				return var;
			}
		}
		
		return null;
	}
	
	
	public boolean isEmpty() {
		return code.isEmpty();
	}
	
	
	public Operation getOperation(int index) {
		return code.get(startIndex + index);
	}
	
	public @Nullable Operation getLastOperation(DecompilationContext context) {
		return code.isEmpty() ? null : code.get(code.size() - 1);
	}
	
	public void addOperation(DecompilationContext context, Operation operation) {
		code.add(operation);
		if(operation instanceof Scope)
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
	public String toString() {
		return this.getClass().getSimpleName() + " {" + startIndex + ", " + endIndex + "}";
	}
}