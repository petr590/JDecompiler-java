package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.CALoadOperation;
import x590.javaclass.operation.Operation;

public class CALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new CALoadOperation(context);
	}
}