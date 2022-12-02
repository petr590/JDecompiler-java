package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.MultiANewArrayOperation;
import x590.javaclass.operation.Operation;

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