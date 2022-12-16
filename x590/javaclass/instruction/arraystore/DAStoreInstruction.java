package x590.javaclass.instruction.arraystore;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.arraystore.DAStoreOperation;

public class DAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DAStoreOperation(context);
	}
}