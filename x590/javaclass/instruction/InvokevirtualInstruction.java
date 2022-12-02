package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.InvokevirtualOperation;
import x590.javaclass.operation.Operation;

public class InvokevirtualInstruction extends InstructionWithIndex {
	
	public InvokevirtualInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokevirtualOperation(context, index);
	}
}