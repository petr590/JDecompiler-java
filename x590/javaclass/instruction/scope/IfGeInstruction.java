package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.compare.CompareType;

public class IfGeInstruction extends IfInstruction {
	
	public IfGeInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public CompareType getCompareType() {
		return CompareType.GREATER_OR_EQUALS;
	}
}