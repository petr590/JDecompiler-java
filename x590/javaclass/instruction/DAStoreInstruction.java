package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DAStoreOperation;
import x590.javaclass.operation.Operation;

public class DAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DAStoreOperation(context);
	}
}