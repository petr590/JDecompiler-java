package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.IReturnOperation;
import x590.javaclass.operation.Operation;

public class IReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IReturnOperation(context);
	}
}