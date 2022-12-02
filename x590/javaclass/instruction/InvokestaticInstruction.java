package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.InvokestaticOperation;
import x590.javaclass.operation.Operation;

public class InvokestaticInstruction extends InstructionWithIndex {
	
	public InvokestaticInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokestaticOperation(context, index);
	}
}