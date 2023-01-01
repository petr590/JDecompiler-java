package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareBinaryOperation;
import x590.javaclass.operation.condition.ConditionOperation;
import x590.javaclass.type.PrimitiveType;

public abstract class IfICmpInstruction extends IfInstruction {
	
	public IfICmpInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public ConditionOperation getCondition(DecompilationContext context) {
		return new CompareBinaryOperation(context, getCompareType(), PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN);
	}
}