package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareType;
import x590.jdecompiler.operation.condition.CompareType.EqualsCompareType;

public class IfNullInstruction extends IfANullInstruction {
	
	public IfNullInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public EqualsCompareType getCompareType() {
		return CompareType.EQUALS;
	}
}
