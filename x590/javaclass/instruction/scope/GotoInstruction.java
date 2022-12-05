package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.scope.ElseScope;
import x590.javaclass.scope.EmptyInfiniteLoopScope;
import x590.javaclass.scope.IfScope;
import x590.javaclass.scope.Scope;

public class GotoInstruction extends EndPosInstruction {
	
	public GotoInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		int endIndex = context.posToIndex(endPos);
		int currentIndex = context.currentIndex();
		Scope currentScope = context.currentScope();
		
		if(endIndex > currentIndex) {
			if(currentScope instanceof IfScope && currentIndex + 1 == currentScope.endIndex()) {
				return new ElseScope(context, endIndex, (IfScope)currentScope);
			}
			
		} else if(endIndex < currentIndex) {
			// loop
			
		} else {
			return new EmptyInfiniteLoopScope(context);
		}
		
		return null;
	}
}