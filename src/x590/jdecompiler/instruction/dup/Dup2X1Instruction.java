package x590.jdecompiler.instruction.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.other.Dup;

public class Dup2X1Instruction extends AbstractDupInstruction {
	
	@Override
	protected void dup(DecompilationContext context) {
		Dup.dup2X1(context);
	}
}
