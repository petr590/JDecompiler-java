package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.AThrowOperation;
import x590.javaclass.operation.Operation;

public class AThrowInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AThrowOperation(context);
	}
}