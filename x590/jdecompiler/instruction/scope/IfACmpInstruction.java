package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.CompareBinaryOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.type.Types;

public abstract class IfACmpInstruction extends IfInstruction {
	
	public IfACmpInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public ConditionOperation getCondition(DecompilationContext context) {
		return new CompareBinaryOperation(context, getCompareType(), Types.ANY_OBJECT_TYPE);
	}
}
