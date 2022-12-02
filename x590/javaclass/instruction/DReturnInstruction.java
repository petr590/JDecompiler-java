package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DReturnOperation;
import x590.javaclass.operation.Operation;

public class DReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DReturnOperation(context);
	}
}