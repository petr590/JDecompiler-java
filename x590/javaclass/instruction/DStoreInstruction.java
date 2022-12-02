package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DStoreOperation;
import x590.javaclass.operation.Operation;

public class DStoreInstruction extends StoreInstruction {
	
	public DStoreInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DStoreOperation(context, index);
	}
}