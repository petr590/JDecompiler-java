package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.IAStoreOperation;
import x590.javaclass.operation.Operation;

public class IAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IAStoreOperation(context);
	}
}