package x590.javaclass.instruction.field;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.field.PutInstanceFieldOperation;

public class PutInstanceFieldInstruction extends InstructionWithIndex {
	
	public PutInstanceFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PutInstanceFieldOperation(context, index);
	}
}