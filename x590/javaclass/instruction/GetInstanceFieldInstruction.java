package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.GetInstanceFieldOperation;
import x590.javaclass.operation.Operation;

public class GetInstanceFieldInstruction extends InstructionWithIndex {
	
	public GetInstanceFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new GetInstanceFieldOperation(context, index);
	}
}