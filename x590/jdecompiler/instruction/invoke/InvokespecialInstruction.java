package x590.jdecompiler.instruction.invoke;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.invoke.InvokespecialOperation;

public final class InvokespecialInstruction extends InstructionWithIndex {
	
	public InvokespecialInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokespecialOperation(context, index);
	}
}
