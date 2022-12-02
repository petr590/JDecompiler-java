package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DupX1Operation;
import x590.javaclass.operation.Operation;

public class DupX1Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DupX1Operation(context);
	}
}