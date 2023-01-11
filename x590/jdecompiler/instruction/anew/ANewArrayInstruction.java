package x590.jdecompiler.instruction.anew;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.anew.ANewArrayOperation;

public class ANewArrayInstruction extends InstructionWithIndex {
	
	public ANewArrayInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ANewArrayOperation(context, index);
	}
}
