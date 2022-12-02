package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.LAStoreOperation;
import x590.javaclass.operation.Operation;

public class LAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LAStoreOperation(context);
	}
}