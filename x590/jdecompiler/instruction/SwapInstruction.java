package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.TypeSize;

public class SwapInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		Operation value1 = context.stack.popWithSize(TypeSize.FOUR_BYTES);
		Operation value2 = context.stack.popWithSize(TypeSize.FOUR_BYTES);
		
		context.stack.push(value1);
		context.stack.push(value2);
		
		return null;
	}
}
