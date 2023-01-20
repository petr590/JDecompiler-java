package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.scope.Scope;

public abstract class ScopeInstruction extends Instruction {
	
	@Override
	public final Operation toOperation(DecompilationContext context) {
		return toScope(context);
	}
	
	@Override
	public abstract Scope toScope(DecompilationContext context);
}
