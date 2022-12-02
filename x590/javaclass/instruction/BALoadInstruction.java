package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.BALoadOperation;
import x590.javaclass.operation.Operation;

public class BALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new BALoadOperation(context);
	}
}