package x590.jdecompiler.operation;

import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.variable.Variable;

public abstract class OperationWithVariable extends ReturnableOperation {
	
	protected final Variable variable;
	
	public OperationWithVariable(Variable variable) {
		super(PrimitiveType.VOID);
		this.variable = variable;
	}
	
	public Variable getVariable() {
		return variable;
	}
}
