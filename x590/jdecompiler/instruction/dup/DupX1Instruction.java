package x590.jdecompiler.instruction.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.dup.DupX1Operation;

public class DupX1Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DupX1Operation(context);
	}
}
