package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.scope.SynchronizedScope;
import x590.javaclass.scope.Scope;

public class MonitorEnterInstruction extends ScopeInstruction {
	
	@Override
	public Scope toScope(DecompilationContext context) {
		return new SynchronizedScope(context);
	}
}