package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.FAStoreOperation;
import x590.javaclass.operation.Operation;

public class FAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FAStoreOperation(context);
	}
}