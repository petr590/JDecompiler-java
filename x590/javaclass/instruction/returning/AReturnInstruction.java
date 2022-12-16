package x590.javaclass.instruction.returning;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.returning.AReturnOperation;

public class AReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AReturnOperation(context);
	}
}