package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.CAStoreOperation;
import x590.javaclass.operation.Operation;

public class CAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new CAStoreOperation(context);
	}
}