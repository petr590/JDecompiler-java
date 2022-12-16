package x590.javaclass.instruction.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.store.AStoreOperation;

public class AStoreInstruction extends StoreInstruction {
	
	public AStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AStoreOperation(context, index);
	}
}