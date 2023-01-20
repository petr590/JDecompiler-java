package x590.jdecompiler.instruction.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.load.LLoadOperation;

public class LLoadInstruction extends LoadInstruction {
	
	public LLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LLoadOperation(context, index);
	}
}
