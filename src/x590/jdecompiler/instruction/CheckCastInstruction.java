package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.ObjectCastOperation;

public class CheckCastInstruction extends InstructionWithIndex {
	
	public CheckCastInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ObjectCastOperation(context, index);
	}
}
