package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DCmpOperation;
import x590.javaclass.operation.Operation;

public class DCmpInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DCmpOperation(context);
	}
}