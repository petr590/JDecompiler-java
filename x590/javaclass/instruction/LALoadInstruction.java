package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.LALoadOperation;
import x590.javaclass.operation.Operation;

public class LALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LALoadOperation(context);
	}
}