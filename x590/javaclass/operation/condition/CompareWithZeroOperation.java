package x590.javaclass.operation.condition;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.Priority;
import x590.javaclass.type.PrimitiveType;

public class CompareWithZeroOperation extends CompareUnaryOperation {
	
	public CompareWithZeroOperation(Operation operand, CompareType compareType) {
		super(operand, compareType);
		operand.castReturnTypeToNarrowest(PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN);
		operand.allowImplicitCast();
	}
	
	private boolean isBooleanType() {
		return compareType.isEqualsCompareType && operand.getReturnType().isSubtypeOf(PrimitiveType.BOOLEAN);
	}
	
	@Override
	public int getPriority() {
		return isBooleanType() ? Priority.BIT_NOT : super.getPriority();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(isBooleanType()) // write `!bool` instead of `bool == false`
			out.print(((EqualsCompareType)compareType).getUnaryOperator(inverted)).printPrioritied(this, operand, context, Associativity.RIGHT);
		else
			out.printPrioritied(this, operand, context, Associativity.LEFT).printsp().print(compareType.getOperator(inverted)).print(" 0");
	}
}