package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareBinaryOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.type.PrimitiveType;

public abstract class IfICmpInstruction extends IfInstruction {
	
	public IfICmpInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public ConditionOperation getCondition(DecompilationContext context) {
		return new CompareBinaryOperation(context, getCompareType(), PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN);
	}
}
