package x590.jdecompiler.instruction.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.arraystore.DAStoreOperation;

public class DAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DAStoreOperation(context);
	}
}
