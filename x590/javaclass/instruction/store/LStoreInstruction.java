package x590.javaclass.instruction.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.store.LStoreOperation;

public class LStoreInstruction extends StoreInstruction {
	
	public LStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LStoreOperation(context, index);
	}
}