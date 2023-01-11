package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.main.JDecompiler;

public abstract class IfElseScope extends Scope {
	
	public IfElseScope(DecompilationContext context, int endIndex) {
		super(context, endIndex);
	}
	
	public IfElseScope(DecompilationContext context, int startIndex, int endIndex) {
		super(context, startIndex, endIndex);
	}
	
	public IfElseScope(DecompilationContext context, int startIndex, Scope superScope) {
		super(context, startIndex, superScope);
	}
	
	public IfElseScope(int startIndex, int endIndex, Scope superScope) {
		super(startIndex, endIndex, superScope);
	}
	
	protected boolean canSelfOmitCurlyBrackets() {
		return JDecompiler.getInstance().canOmitCurlyBrackets() &&
				(code.isEmpty() || code.size() == 1 && code.get(0).isScope() && ((Scope)code.get(0)).canOmitCurlyBrackets());
	}
}
