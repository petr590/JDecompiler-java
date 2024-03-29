package x590.jdecompiler.instruction.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.returning.VReturnOperation;

public class VReturnInstruction implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return VReturnOperation.getInstance(context);
	}
}
