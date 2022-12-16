package x590.javaclass.instruction.returning;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.returning.VReturnOperation;

public class VReturnInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return VReturnOperation.INSTANCE;
	}
}