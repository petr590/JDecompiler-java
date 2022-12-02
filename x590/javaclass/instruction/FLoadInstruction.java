package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.FLoadOperation;
import x590.javaclass.operation.Operation;

public class FLoadInstruction extends LoadInstruction {
	
	public FLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FLoadOperation(context, index);
	}
}