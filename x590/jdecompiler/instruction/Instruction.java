package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.util.annotation.Nullable;

public abstract class Instruction {
	
	public Instruction() {}
	
	public abstract @Nullable Operation toOperation(DecompilationContext context);
	
	public @Nullable Scope toScope(DecompilationContext context) {
		return null;
	}
	
	public void postDecompilation(DecompilationContext context) {}
}