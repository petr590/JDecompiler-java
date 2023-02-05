package x590.jdecompiler.instruction.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.returning.DReturnOperation;

public class DReturnInstruction implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DReturnOperation(context);
	}
}
