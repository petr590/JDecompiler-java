package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.type.TypeSize;

public class SwapInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		Operation value1 = context.popWithSize(TypeSize.WORD);
		Operation value2 = context.popWithSize(TypeSize.WORD);
		
		context.push(value1);
		context.push(value2);
		
		return null;
	}
}
