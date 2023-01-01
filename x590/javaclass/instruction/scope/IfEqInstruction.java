package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareType;

public class IfEqInstruction extends IfInstruction {
	
	public IfEqInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.EQUALS;
	}
}