package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;

public abstract class BinaryConditionOperation extends ConditionOperation {
	
	public final ConditionOperation operand1, operand2;
	
	public BinaryConditionOperation(ConditionOperation operand1, ConditionOperation operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
	}
	
	@Override
	protected void onInvert() {
		operand1.invert();
		operand2.invert();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out .printPrioritied(this, operand1, context, Associativity.LEFT)
			.print(getOperator())
			.printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
	
	protected abstract String getOperator();
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof BinaryConditionOperation operation &&
				getOperator().equals(operation.getOperator()) &&
				operand1.equals(operation.operand1) && operand2.equals(operation.operand2);
	}
}
