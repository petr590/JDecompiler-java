package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.LStoreOperation;
import x590.javaclass.operation.Operation;

public class LStoreInstruction extends StoreInstruction {
	
	public LStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LStoreOperation(context, index);
	}
}