package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.FStoreOperation;
import x590.javaclass.operation.Operation;

public class FStoreInstruction extends StoreInstruction {
	
	public FStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FStoreOperation(context, index);
	}
}