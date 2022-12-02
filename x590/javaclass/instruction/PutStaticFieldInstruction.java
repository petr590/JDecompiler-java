package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.PutStaticFieldOperation;
import x590.javaclass.operation.Operation;

public class PutStaticFieldInstruction extends InstructionWithIndex {
	
	public PutStaticFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PutStaticFieldOperation(context, index);
	}
}