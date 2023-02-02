package x590.jdecompiler.instruction.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.arraystore.BAStoreOperation;

public class BAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new BAStoreOperation(context);
	}
}
