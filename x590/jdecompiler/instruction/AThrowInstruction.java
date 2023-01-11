package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.AThrowOperation;
import x590.jdecompiler.operation.Operation;

public class AThrowInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AThrowOperation(context);
	}
}
