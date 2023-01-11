package x590.jdecompiler.instruction.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.load.DLoadOperation;

public class DLoadInstruction extends LoadInstruction {
	
	public DLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DLoadOperation(context, index);
	}
}
