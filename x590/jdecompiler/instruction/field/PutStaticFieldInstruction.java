package x590.jdecompiler.instruction.field;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.field.PutStaticFieldOperation;

public class PutStaticFieldInstruction extends InstructionWithIndex {
	
	public PutStaticFieldInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PutStaticFieldOperation(context, index);
	}
}
