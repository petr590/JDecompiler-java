package x590.javaclass.operation.compare;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;

public class CompareWithZeroOperation extends CompareUnaryOperation {
	
	public CompareWithZeroOperation(Operation operand, CompareType compareType) {
		super(operand, compareType);
		operand.allowImplicitCast();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(operand.getReturnType().isSubtypeOf(PrimitiveType.BOOLEAN) && compareType.isEqualsCompareType) // write `!bool` instead of `bool == false`
			out.print(((EqualsCompareType)compareType).getUnaryOperator(inverted)).printPrioritied(this, operand, context, Associativity.RIGHT);
		else
			out.printPrioritied(this, operand, context, Associativity.LEFT).printsp().print(compareType.getOperator(inverted)).print(" 0");
	}
}