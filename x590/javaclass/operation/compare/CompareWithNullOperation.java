package x590.javaclass.operation.compare;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;

public class CompareWithNullOperation extends CompareUnaryOperation {
	
	public CompareWithNullOperation(Operation operand, EqualsCompareType compareType) {
		super(operand, compareType);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(operand, context).printsp().print(compareType.getOperator(inverted)).print(" null");
	}
}