package x590.jdecompiler.instruction.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.other.Dup;

public class DupX2Instruction extends AbstractDupInstruction {
	
	@Override
	protected void dup(DecompilationContext context) {
		Dup.dupX2(context);
	}
}
