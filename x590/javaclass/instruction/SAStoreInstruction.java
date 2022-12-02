package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.SAStoreOperation;
import x590.javaclass.operation.Operation;

public class SAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new SAStoreOperation(context);
	}
}