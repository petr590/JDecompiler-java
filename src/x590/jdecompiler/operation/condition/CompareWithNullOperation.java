package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.CompareType.EqualsCompareType;
import x590.jdecompiler.type.Types;

public final class CompareWithNullOperation extends CompareUnaryOperation {
	
	public CompareWithNullOperation(Operation operand, EqualsCompareType compareType) {
		super(operand.useAsNarrowest(Types.ANY_OBJECT_TYPE), compareType);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand, context, Associativity.LEFT).printsp()
			.print(getCompareType().getOperator(inverted)).printsp()
			.print("null");
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof CompareWithNullOperation operation &&
				operand.equals(operation.operand);
	}
}
