package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.LCmpOperation;
import x590.javaclass.operation.Operation;

public class LCmpInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LCmpOperation(context);
	}
}