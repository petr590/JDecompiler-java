package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareWithNullOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.condition.CompareType.EqualsCompareType;

public abstract class IfANullInstruction extends IfInstruction {
	
	public IfANullInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public ConditionOperation getCondition(DecompilationContext context) {
		return new CompareWithNullOperation(context.pop(), getCompareType());
	}
	
	@Override
	public abstract EqualsCompareType getCompareType();
}
