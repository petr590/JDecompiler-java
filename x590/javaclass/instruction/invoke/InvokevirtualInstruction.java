package x590.javaclass.instruction.invoke;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.invoke.InvokevirtualOperation;

public class InvokevirtualInstruction extends InstructionWithIndex {
	
	public InvokevirtualInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokevirtualOperation(context, index);
	}
}