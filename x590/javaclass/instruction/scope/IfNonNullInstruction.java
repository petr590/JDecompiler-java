package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareType;
import x590.javaclass.operation.condition.EqualsCompareType;

public class IfNonNullInstruction extends IfANullInstruction {
	
	public IfNonNullInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public EqualsCompareType getCompareType() {
		return CompareType.NOT_EQUALS;
	}
}