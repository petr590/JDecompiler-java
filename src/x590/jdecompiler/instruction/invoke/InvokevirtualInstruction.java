package x590.jdecompiler.instruction.invoke;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.invoke.InvokevirtualOperation;

public final class InvokevirtualInstruction extends InstructionWithIndex {
	
	public InvokevirtualInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return InvokevirtualOperation.operationOf(context, index);
	}
}
