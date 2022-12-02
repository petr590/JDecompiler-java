package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.BAStoreOperation;
import x590.javaclass.operation.Operation;

public class BAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new BAStoreOperation(context);
	}
}