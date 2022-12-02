package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.IIncOperatin;
import x590.javaclass.operation.Operation;

public class IIncInstruction extends InstructionWithIndex {
	
	private final int value;
	
	public IIncInstruction(int index, int value) {
		super(index);
		this.value = value;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IIncOperatin(context, index, value);
	}
}