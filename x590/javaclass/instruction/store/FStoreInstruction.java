package x590.javaclass.instruction.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.store.FStoreOperation;

public class FStoreInstruction extends StoreInstruction {
	
	public FStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FStoreOperation(context, index);
	}
}