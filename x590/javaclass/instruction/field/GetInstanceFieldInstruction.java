package x590.javaclass.instruction.field;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.field.GetInstanceFieldOperation;

public class GetInstanceFieldInstruction extends InstructionWithIndex {
	
	public GetInstanceFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new GetInstanceFieldOperation(context, index);
	}
}