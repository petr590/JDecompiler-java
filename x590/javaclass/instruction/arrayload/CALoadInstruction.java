package x590.javaclass.instruction.arrayload;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.arrayload.CALoadOperation;

public class CALoadInstruction extends ArrayLoadInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new CALoadOperation(context);
	}
}