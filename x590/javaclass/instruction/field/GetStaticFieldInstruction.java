package x590.javaclass.instruction.field;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.field.GetStaticFieldOperation;

public class GetStaticFieldInstruction extends InstructionWithIndex {
	
	public GetStaticFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new GetStaticFieldOperation(context, index);
	}
}