package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.CheckCastOperation;
import x590.javaclass.operation.Operation;

public class CheckCastInstruction extends InstructionWithIndex {
	
	public CheckCastInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new CheckCastOperation(context, index);
	}
}