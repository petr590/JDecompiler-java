package x590.javaclass.instruction.dup;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.dup.Dup2X1Operation;

public class Dup2X1Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new Dup2X1Operation(context);
	}
}