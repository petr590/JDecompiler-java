package x590.jdecompiler.instruction.other;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.other.NewOperation;

public class NewInstruction extends InstructionWithIndex {
	
	public NewInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new NewOperation(context, index);
	}
}
