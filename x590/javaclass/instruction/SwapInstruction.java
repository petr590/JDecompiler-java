package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.TypeSize;

public class SwapInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		Operation value1 = context.stack.popWithSize(TypeSize.EIGHT_BYTES);
		Operation value2 = context.stack.popWithSize(TypeSize.EIGHT_BYTES);
		
		context.stack.push(value1);
		context.stack.push(value2);
		
		return null;
	}
}