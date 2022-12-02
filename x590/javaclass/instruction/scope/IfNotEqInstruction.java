package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.compare.CompareOperation;
import x590.javaclass.operation.compare.CompareType;
import x590.javaclass.operation.compare.ConditionOperation;

public class IfNotEqInstruction extends IfInstruction {
	
	public IfNotEqInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public ConditionOperation getCondition(DecompilationContext context) {
		return CompareOperation.valueOf(context, CompareType.NOT_EQUALS);
	}
}