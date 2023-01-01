package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareType;

public class IfNotEqInstruction extends IfInstruction {
	
	public IfNotEqInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.NOT_EQUALS;
	}
}