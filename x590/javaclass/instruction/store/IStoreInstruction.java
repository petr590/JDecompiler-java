package x590.javaclass.instruction.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.store.IStoreOperation;

public class IStoreInstruction extends StoreInstruction {
	
	public IStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IStoreOperation(context, index);
	}
}