package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.InstanceofOperation;
import x590.jdecompiler.operation.Operation;

public class InstanceofInstruction extends InstructionWithIndex {
	
	public InstanceofInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InstanceofOperation(context, index);
	}	
}
