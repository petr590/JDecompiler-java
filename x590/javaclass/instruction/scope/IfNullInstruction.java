package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareType;
import x590.javaclass.operation.condition.EqualsCompareType;

public class IfNullInstruction extends IfANullInstruction {
	
	public IfNullInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public EqualsCompareType getCompareType() {
		return CompareType.EQUALS;
	}
}