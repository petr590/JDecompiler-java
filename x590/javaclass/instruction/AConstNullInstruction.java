package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.AConstNullOperation;
import x590.javaclass.operation.Operation;

public class AConstNullInstruction extends Instruction {
	
	public static final AConstNullInstruction INSTANCE = new AConstNullInstruction();
	
	private AConstNullInstruction() {}
	
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return AConstNullOperation.INSTANCE;
	}
}