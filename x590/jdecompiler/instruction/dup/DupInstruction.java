package x590.jdecompiler.instruction.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.dup.DupOperation;

public class DupInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DupOperation(context);
	}
}
