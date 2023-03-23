package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.PreDecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.util.annotation.Nullable;

/**
 * Описывает инструкцию байткода
 */
public interface Instruction {
	
	public @Nullable Operation toOperation(DecompilationContext context);
	
	public default void preDecompilation(PreDecompilationContext context) {}
	public default void postDecompilation(DecompilationContext context) {}
}
