package x590.jdecompiler.instruction.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.arrayload.IALoadOperation;

public class IALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IALoadOperation(context);
	}
}
