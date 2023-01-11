package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareType;

public class IfIGtInstruction extends IfICmpInstruction {
	
	public IfIGtInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.GREATER;
	}
}
