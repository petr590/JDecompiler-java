package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.SALoadOperation;
import x590.javaclass.operation.Operation;

public class SALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new SALoadOperation(context);
	}
}