package x590.javaclass.instruction.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.load.LLoadOperation;

public class LLoadInstruction extends LoadInstruction {
	
	public LLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LLoadOperation(context, index);
	}
}