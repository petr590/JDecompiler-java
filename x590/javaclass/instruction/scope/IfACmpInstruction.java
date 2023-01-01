package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.condition.CompareBinaryOperation;
import x590.javaclass.operation.condition.ConditionOperation;
import x590.javaclass.type.Types;

public abstract class IfACmpInstruction extends IfInstruction {
	
	public IfACmpInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public ConditionOperation getCondition(DecompilationContext context) {
		return new CompareBinaryOperation(context, getCompareType(), Types.ANY_OBJECT_TYPE);
	}
}