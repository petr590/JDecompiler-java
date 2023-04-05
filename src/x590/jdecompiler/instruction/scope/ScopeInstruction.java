package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.util.annotation.Nullable;

public abstract class ScopeInstruction implements Instruction {
	
	@Override
	public @Nullable Operation toOperation(DecompilationContext context) {
		return toScope(context);
	}
	
	protected abstract @Nullable Scope toScope(DecompilationContext context);
}
