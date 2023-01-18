package x590.jdecompiler.instruction.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.store.LStoreOperation;

public class LStoreInstruction extends StoreInstruction {
	
	public LStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LStoreOperation(context, index);
	}
}
