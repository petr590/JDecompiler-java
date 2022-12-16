package x590.javaclass.instruction.cmp;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.cmp.DCmpOperation;

public class DCmpInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DCmpOperation(context);
	}
}