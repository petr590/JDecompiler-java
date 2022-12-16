package x590.javaclass.instruction.invoke;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.invoke.InvokespecialOperation;

public class InvokespecialInstruction extends InstructionWithIndex {
	
	public InvokespecialInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokespecialOperation(context, index);
	}
}