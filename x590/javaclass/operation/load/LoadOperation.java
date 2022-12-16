package x590.javaclass.operation.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;
import x590.javaclass.variable.Variable;

public class LoadOperation extends Operation {
	
	protected final int index;
	protected final Variable variable;
	
//	private Operation incOperation = null;
	
	public LoadOperation(Type type, DecompilationContext context, int index) {
		this.index = index;
		this.variable = context.currentScope().getDefinedVariable(index);
		variable.castTypeToNarrowest(type);
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
//		return incOperation != null ? incOperation->toString(context) : context.getCurrentScope().getNameFor(variable);
		out.write(variable.getName());
	}
	
	@Override
	public Type getReturnType() {
		return variable.getType();
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		variable.castTypeToNarrowest(newType);
	}
	
	@Override
	public void addVariableName(String name) {
		variable.addName(name);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return true;
	}
}