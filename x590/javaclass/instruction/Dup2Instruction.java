package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Dup2Operation;
import x590.javaclass.operation.Operation;

public class Dup2Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new Dup2Operation(context);
	}
}