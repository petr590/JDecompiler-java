package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.InstanceofOperation;
import x590.javaclass.operation.Operation;

public class InstanceofInstruction extends InstructionWithIndex {
	
	public InstanceofInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InstanceofOperation(context, index);
	}	
}