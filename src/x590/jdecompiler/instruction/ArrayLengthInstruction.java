package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.array.ArrayLengthOperation;

public class ArrayLengthInstruction implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ArrayLengthOperation(context);
	}
}
