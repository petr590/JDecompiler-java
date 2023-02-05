package x590.jdecompiler.instruction.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;

public abstract class AbstractDupInstruction implements Instruction {
	
	@Override
	public final Operation toOperation(DecompilationContext context) {
		dup(context);
		return null;
	}
	
	protected abstract void dup(DecompilationContext context);
}