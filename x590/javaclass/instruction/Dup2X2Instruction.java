package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Dup2X2Operation;
import x590.javaclass.operation.Operation;

public class Dup2X2Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new Dup2X2Operation(context);
	}
}