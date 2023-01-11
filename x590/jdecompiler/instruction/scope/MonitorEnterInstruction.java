package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.scope.SynchronizedScope;
import x590.jdecompiler.scope.Scope;

public class MonitorEnterInstruction extends ScopeInstruction {
	
	@Override
	public Scope toScope(DecompilationContext context) {
		return new SynchronizedScope(context);
	}
}
