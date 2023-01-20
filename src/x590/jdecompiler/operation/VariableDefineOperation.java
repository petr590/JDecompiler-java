package x590.jdecompiler.operation;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.variable.Variable;

public final class VariableDefineOperation extends VoidOperation {
	
	private final Variable variable;
	
	public VariableDefineOperation(Variable variable) {
		this.variable = variable;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(variable.getType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printsp(variable.getType(), context.classinfo).print(variable.getName());
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof VariableDefineOperation operation &&
				variable.equals(operation.variable);
	}
}
