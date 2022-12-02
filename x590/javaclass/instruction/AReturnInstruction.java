package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.AReturnOperation;
import x590.javaclass.operation.Operation;

public class AReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AReturnOperation(context);
	}
}