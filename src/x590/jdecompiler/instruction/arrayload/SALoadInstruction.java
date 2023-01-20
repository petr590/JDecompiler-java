package x590.jdecompiler.instruction.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.arrayload.SALoadOperation;

public class SALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new SALoadOperation(context);
	}
}
