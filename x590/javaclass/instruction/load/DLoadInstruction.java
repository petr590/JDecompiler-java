package x590.javaclass.instruction.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.load.DLoadOperation;

public class DLoadInstruction extends LoadInstruction {
	
	public DLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DLoadOperation(context, index);
	}
}