package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareType;

public class IfLtInstruction extends IfInstruction {
	
	public IfLtInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.LESS;
	}
}
