package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.NewOperation;
import x590.javaclass.operation.Operation;

public class NewInstruction extends InstructionWithIndex {
	
	public NewInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new NewOperation(context, index);
	}
}