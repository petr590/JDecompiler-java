package x590.javaclass.instruction.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.load.ILoadOperation;

public class ILoadInstruction extends LoadInstruction {
	
	public ILoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ILoadOperation(context, index);
	}
}