package x590.jdecompiler.operation.condition;

import x590.jdecompiler.exception.Operation;

public abstract class CompareUnaryOperation extends CompareOperation {
	
	protected final Operation operand;
	
	public CompareUnaryOperation(Operation operand, CompareType compareType) {
		super(compareType);
		this.operand = operand;
	}
}
