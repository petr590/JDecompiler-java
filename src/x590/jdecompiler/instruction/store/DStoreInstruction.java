package x590.jdecompiler.instruction.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.store.DStoreOperation;

public class DStoreInstruction extends StoreInstruction {
	
	public DStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DStoreOperation(context, index);
	}
}
