package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DupX2Operation;
import x590.javaclass.operation.Operation;

public class DupX2Instruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DupX2Operation(context);
	}
}