package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.PutInstanceFieldOperation;
import x590.javaclass.operation.Operation;

public class PutInstanceFieldInstruction extends InstructionWithIndex {
	
	public PutInstanceFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PutInstanceFieldOperation(context, index);
	}
}