package x590.jdecompiler.operation.variable;

import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.variable.Variable;

public abstract class OperationWithVariable extends ReturnableOperation {
	
	protected Variable variable;
	
	public OperationWithVariable(Variable variable) {
		this();
		this.variable = variable;
	}
	
	public OperationWithVariable() {
		super(PrimitiveType.VOID);
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	protected boolean equals(OperationWithVariable other) {
		return super.equals(other) && variable.equals(other.variable);
	}
}
