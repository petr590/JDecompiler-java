package x590.jdecompiler.instruction.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.cmp.DCmpOperation;

public class DCmpInstruction extends Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DCmpOperation(context);
	}
}
