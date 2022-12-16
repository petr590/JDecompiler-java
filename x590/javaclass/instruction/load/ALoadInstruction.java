package x590.javaclass.instruction.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.load.ALoadOperation;

public class ALoadInstruction extends LoadInstruction {
	
	public ALoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ALoadOperation(context, index);
	}
}