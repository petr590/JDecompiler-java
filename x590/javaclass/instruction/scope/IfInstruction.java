package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.compare.AndOperation;
import x590.javaclass.operation.compare.CompareOperation;
import x590.javaclass.operation.compare.CompareType;
import x590.javaclass.operation.compare.ConditionOperation;
import x590.javaclass.operation.compare.OrOperation;
import x590.javaclass.scope.IfScope;
import x590.javaclass.scope.Scope;

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