package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Dup2X1Operation;
import x590.javaclass.operation.Operation;

public class Dup2X1Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new Dup2X1Operation(context);
	}
}