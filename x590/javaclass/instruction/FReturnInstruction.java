package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.FReturnOperation;
import x590.javaclass.operation.Operation;

public class FReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FReturnOperation(context);
	}
}