package x590.jdecompiler.instruction.field;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.field.GetStaticFieldOperation;

public class GetStaticFieldInstruction extends InstructionWithIndex {
	
	public GetStaticFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new GetStaticFieldOperation(context, index);
	}
}
