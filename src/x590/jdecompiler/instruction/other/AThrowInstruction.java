package x590.jdecompiler.instruction.other;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.other.AThrowOperation;

public class AThrowInstruction implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AThrowOperation(context);
	}
}
