package x590.jdecompiler.instruction.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.load.ALoadOperation;

public class ALoadInstruction extends LoadInstruction {
	
	public ALoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ALoadOperation(context, index);
	}
}
