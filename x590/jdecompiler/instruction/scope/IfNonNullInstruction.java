package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareType;
import x590.jdecompiler.operation.condition.CompareType.EqualsCompareType;

public class IfNonNullInstruction extends IfANullInstruction {
	
	public IfNonNullInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public EqualsCompareType getCompareType() {
		return CompareType.NOT_EQUALS;
	}
}
