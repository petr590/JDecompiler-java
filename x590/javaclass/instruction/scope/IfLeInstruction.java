package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.compare.CompareType;

public class IfLeInstruction extends IfInstruction {
	
	public IfLeInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.LESS_OR_EQUALS;
	}
}