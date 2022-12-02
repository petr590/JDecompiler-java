package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DupOperation;
import x590.javaclass.operation.Operation;

public class DupInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DupOperation(context);
	}
}