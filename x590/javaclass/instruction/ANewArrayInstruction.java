package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.ANewArrayOperation;
import x590.javaclass.operation.Operation;

public class ANewArrayInstruction extends InstructionWithIndex {
	
	public ANewArrayInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ANewArrayOperation(context, index);
	}
}