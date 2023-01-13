package x590.jdecompiler.instruction.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Dup;

public class Dup2X2Instruction extends AbstractDupInstruction {
	
	@Override
	protected void dup(DecompilationContext context) {
		Dup.dup2X2(context);
	}
}
