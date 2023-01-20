package x590.jdecompiler.instruction.anew;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.array.MultiANewArrayOperation;

public class MultiANewArrayInstruction extends InstructionWithIndex {
	
	private final int dimensions;
	
	public MultiANewArrayInstruction(int index, int dimensions) {
		super(index);
		this.dimensions = dimensions;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new MultiANewArrayOperation(context, index, dimensions);
	}
}
