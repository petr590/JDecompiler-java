package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.compare.CompareOperation;
import x590.javaclass.operation.compare.CompareType;
import x590.javaclass.operation.compare.ConditionOperation;
import x590.javaclass.scope.IfScope;
import x590.javaclass.scope.Scope;

public abstract class IfInstruction extends EndPosInstruction {
	
	public IfInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		return new IfScope(context, context.posToIndex(endPos), getCondition(context));
	}

	public ConditionOperation getCondition(DecompilationContext context) {
		return CompareOperation.valueOf(context, getCompareType());
	}
	
	public abstract CompareType getCompareType();
}