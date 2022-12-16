package x590.javaclass.operation;

import x590.javaclass.type.PrimitiveType;
import x590.javaclass.variable.Variable;

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