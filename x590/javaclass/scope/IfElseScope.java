package x590.javaclass.scope;

import x590.javaclass.context.DecompilationContext;

public abstract class IfElseScope extends Scope {
	
	public IfElseScope(int startIndex, int endIndex) {
		super(startIndex, endIndex);
	}
	
	public IfElseScope(DecompilationContext context, int endIndex) {
		super(context, endIndex);
	}
	
	protected boolean canSelfOmitCurlyBrackets() {
		return super.canOmitCurlyBrackets();
	}
}
