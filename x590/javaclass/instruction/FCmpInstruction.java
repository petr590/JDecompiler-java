package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.FCmpOperation;
import x590.javaclass.operation.Operation;

public class FCmpInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FCmpOperation(context);
	}
}