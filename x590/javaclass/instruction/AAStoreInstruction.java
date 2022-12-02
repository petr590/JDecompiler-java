package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.AAStoreOperation;
import x590.javaclass.operation.Operation;

public class AAStoreInstruction extends ArrayStoreInstruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AAStoreOperation(context);
	}
}