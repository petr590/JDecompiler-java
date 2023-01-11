package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareType;

public class IfILtInstruction extends IfICmpInstruction {
	
	public IfILtInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.LESS;
	}
}
