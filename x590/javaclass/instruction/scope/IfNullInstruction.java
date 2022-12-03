package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.compare.CompareType;
import x590.javaclass.operation.compare.EqualsCompareType;

public class IfNullInstruction extends IfANullInstruction {
	
	public IfNullInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public EqualsCompareType getCompareType() {
		return CompareType.EQUALS;
	}
}