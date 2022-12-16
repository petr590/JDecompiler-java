package x590.javaclass.instruction.cmp;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.cmp.LCmpOperation;

public class LCmpInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LCmpOperation(context);
	}
}