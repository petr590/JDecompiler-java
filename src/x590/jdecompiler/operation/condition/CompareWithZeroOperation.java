package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.CompareType.EqualsCompareType;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class CompareWithZeroOperation extends CompareUnaryOperation {
	
	public CompareWithZeroOperation(Operation operand, CompareType compareType) {
		super(operand, compareType);
		operand.castReturnTypeToNarrowest(PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN);
		operand.allowImplicitCast();
	}
	
	private boolean isBooleanType() {
		return getCompareType().isEqualsCompareType() && operand.getReturnType().isSubtypeOf(PrimitiveType.BOOLEAN);
	}
	
	@Override
	public int getPriority() {
		return isBooleanType() ? getEqualsCompareType().getUnaryPriority(inverted, operand) : super.getPriority();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(isBooleanType()) // write `!bool` instead of `bool == false`
			out .print(getEqualsCompareType().getUnaryOperator(inverted))
				.printPrioritied(this, operand, context, Associativity.LEFT);
		else
			out .printPrioritied(this, operand, context, Associativity.LEFT)
				.printsp().print(getCompareType().getOperator(inverted)).printsp()
				.print('0');
	}
	
	private EqualsCompareType getEqualsCompareType() {
		return (EqualsCompareType)getCompareType();
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof CompareWithZeroOperation operation &&
				operand.equals(operation.operand);
	}
}
