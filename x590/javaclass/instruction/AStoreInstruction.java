package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.AStoreOperation;
import x590.javaclass.operation.Operation;

public class AStoreInstruction extends StoreInstruction {
	
	public AStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AStoreOperation(context, index);
	}
}