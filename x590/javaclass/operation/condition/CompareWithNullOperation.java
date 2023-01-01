package x590.javaclass.operation.condition;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Types;

public class CompareWithNullOperation extends CompareUnaryOperation {
	
	public CompareWithNullOperation(Operation operand, EqualsCompareType compareType) {
		super(operand, compareType);
		operand.castReturnTypeToNarrowest(Types.ANY_OBJECT_TYPE);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(operand, context).printsp().print(compareType.getOperator(inverted)).print(" null");
	}
}