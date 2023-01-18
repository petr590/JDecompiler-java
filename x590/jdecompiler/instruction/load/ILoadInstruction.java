package x590.jdecompiler.instruction.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.load.ILoadOperation;

public class ILoadInstruction extends LoadInstruction {
	
	public ILoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ILoadOperation(context, index);
	}
}
