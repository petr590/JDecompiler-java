package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.scope.ElseScope;
import x590.javaclass.scope.IfScope;
import x590.javaclass.scope.Scope;

public class GotoInstruction extends EndPosInstruction {
	
	public GotoInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		int endIndex = context.posToIndex(endPos);
		Scope currentScope = context.getCurrentScope();
		
		if(currentScope instanceof IfScope && context.currentIndex() + 1 == currentScope.endIndex) {
			return new ElseScope(context, endIndex, (IfScope)currentScope);
		}
		
		return null;
	}
}