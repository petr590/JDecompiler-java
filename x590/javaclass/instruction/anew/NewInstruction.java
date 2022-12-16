package x590.javaclass.instruction.anew;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.anew.NewOperation;

public class NewInstruction extends InstructionWithIndex {
	
	public NewInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new NewOperation(context, index);
	}
}