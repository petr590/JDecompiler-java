package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.condition.AndOperation;
import x590.jdecompiler.operation.condition.CompareOperation;
import x590.jdecompiler.operation.condition.CompareType;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.condition.OrOperation;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.scope.Scope;

public abstract class IfInstruction extends EndPosInstruction {
	
	public IfInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		int endIndex = context.posToIndex(endPos);
		Scope currentScope = context.currentScope();
		
		if(currentScope instanceof IfScope ifScope) {
			
			if(ifScope.endIndex() == endIndex) {
				ifScope.setConditionAndUpdate(new AndOperation(ifScope.getCondition(), getCondition(context).invert()), context);
				return null;
				
			} else if(ifScope.endIndex() == context.currentIndex() + 1) {
				ifScope.setEndIndex(endIndex);
				ifScope.setConditionAndUpdate(new OrOperation(ifScope.getCondition().invert(), getCondition(context).invert()), context);
				return null;
			}
		}
		
		return new IfScope(context, endIndex, getCondition(context));
	}
	
	public ConditionOperation getCondition(DecompilationContext context) {
		return CompareOperation.valueOf(context, getCompareType());
	}
	
	public abstract CompareType getCompareType();
}
