package x590.jdecompiler.operation.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.variable.Variable;

public abstract class LoadOperation extends Operation {
	
	private final int index;
	private final Variable variable;
	
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
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof LoadOperation operation &&
				variable.equals(operation.variable);
	}
}
