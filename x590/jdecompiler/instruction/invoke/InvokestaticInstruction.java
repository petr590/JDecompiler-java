package x590.jdecompiler.instruction.invoke;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.invoke.InvokestaticOperation;

public final class InvokestaticInstruction extends InstructionWithIndex {
	
	public InvokestaticInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return InvokestaticOperation.operationOf(context, index);
	}
}
