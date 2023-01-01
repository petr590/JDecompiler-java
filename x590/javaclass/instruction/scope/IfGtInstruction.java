package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareType;

public class IfGtInstruction extends IfInstruction {
	
	public IfGtInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.GREATER;
	}
}