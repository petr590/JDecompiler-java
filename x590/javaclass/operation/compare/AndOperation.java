package x590.javaclass.operation.compare;

import x590.javaclass.operation.Priority;

public class AndOperation extends BinaryConditionOperation {
	
	public AndOperation(ConditionOperation operand1, ConditionOperation operand2) {
		super(operand1, operand2);
	}
	
	protected String getOperator() {
		return inverted ? " || " : " && ";
	}
	
	@Override
	public int getPriority() {
		return inverted ? Priority.LOGICAL_OR : Priority.LOGICAL_AND;
	}
}