package x590.jdecompiler.operation.variable;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VoidOperation;
import x590.jdecompiler.variable.Variable;

public final class VariableDefineOperation extends AbstractOperation implements VoidOperation, VariableDefinitionOperation {
	
	private final Variable variable;
	private boolean isTypeHidden;
	
	public VariableDefineOperation(Variable variable) {
		this.variable = variable.defined();
	}
	
	@Override
	public boolean isVariableDefinition() {
		return true;
	}
	
	@Override
	public Variable getVariable() {
		return variable;
	}
	
	@Override
	public void hideTypeDefinition() {
		this.isTypeHidden = true;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(variable.getType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(!isTypeHidden) {
			out.print(variable.getType(), context.getClassinfo(), variable.getName());
		} else {
			out.write(variable.getName());
		}
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof VariableDefineOperation operation &&
				variable.equals(operation.variable);
	}
}
