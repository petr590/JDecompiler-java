package x590.jdecompiler.instruction.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.cmp.LCmpOperation;

public class LCmpInstruction implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LCmpOperation(context);
	}
}
