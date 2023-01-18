package x590.jdecompiler.instruction.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.arraystore.FAStoreOperation;

public class FAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FAStoreOperation(context);
	}
}
