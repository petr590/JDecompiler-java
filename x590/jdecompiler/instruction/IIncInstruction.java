package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.IIncOperation;
import x590.jdecompiler.operation.Operation;

public class IIncInstruction extends InstructionWithIndex {
	
	private final int value;
	
	public IIncInstruction(int index, int value) {
		super(index);
		this.value = value;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IIncOperation(context, index, value);
	}
}