package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.ArrayLengthOperation;
import x590.javaclass.operation.Operation;

public class ArrayLengthInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ArrayLengthOperation(context);
	}
}