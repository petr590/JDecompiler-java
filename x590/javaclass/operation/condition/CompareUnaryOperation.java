package x590.javaclass.operation.condition;

import x590.javaclass.operation.Operation;

public abstract class CompareUnaryOperation extends CompareOperation {
	
	protected final Operation operand;
	
	public CompareUnaryOperation(Operation operand, CompareType compareType) {
		super(compareType);
		this.operand = operand;
	}
}