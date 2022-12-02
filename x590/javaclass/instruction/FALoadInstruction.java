package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.FALoadOperation;
import x590.javaclass.operation.Operation;

public class FALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FALoadOperation(context);
	}
}