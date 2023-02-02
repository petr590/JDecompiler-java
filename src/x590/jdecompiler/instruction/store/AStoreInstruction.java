package x590.jdecompiler.instruction.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.store.AStoreOperation;

public class AStoreInstruction extends StoreInstruction {
	
	public AStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AStoreOperation(context, index);
	}
}
