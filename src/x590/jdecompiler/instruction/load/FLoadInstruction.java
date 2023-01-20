package x590.jdecompiler.instruction.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.load.FLoadOperation;

public class FLoadInstruction extends LoadInstruction {
	
	public FLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FLoadOperation(context, index);
	}
}
