package x590.javaclass.instruction.returning;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.returning.FReturnOperation;

public class FReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FReturnOperation(context);
	}
}