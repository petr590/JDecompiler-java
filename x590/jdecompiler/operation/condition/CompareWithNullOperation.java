package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Types;

public class CompareWithNullOperation extends CompareUnaryOperation {
	
	public CompareWithNullOperation(Operation operand, EqualsCompareType compareType) {
		super(operand, compareType);
		operand.castReturnTypeToNarrowest(Types.ANY_OBJECT_TYPE);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand, context, Associativity.LEFT).printsp().print(compareType.getOperator(inverted)).print(" null");
	}
}
