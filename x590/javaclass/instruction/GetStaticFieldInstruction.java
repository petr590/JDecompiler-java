package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.GetStaticFieldOperation;
import x590.javaclass.operation.Operation;

public class GetStaticFieldInstruction extends InstructionWithIndex {
	
	public GetStaticFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new GetStaticFieldOperation(context, index);
	}
}