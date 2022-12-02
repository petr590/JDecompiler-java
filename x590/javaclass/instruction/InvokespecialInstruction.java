package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.InvokespecialOperation;
import x590.javaclass.operation.Operation;

public class InvokespecialInstruction extends InstructionWithIndex {
	
	public InvokespecialInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokespecialOperation(context, index);
	}
}