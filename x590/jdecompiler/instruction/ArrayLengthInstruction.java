package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.array.ArrayLengthOperation;

public class ArrayLengthInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ArrayLengthOperation(context);
	}
}
