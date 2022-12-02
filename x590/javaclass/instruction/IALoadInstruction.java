package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.IALoadOperation;
import x590.javaclass.operation.Operation;

public class IALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IALoadOperation(context);
	}
}