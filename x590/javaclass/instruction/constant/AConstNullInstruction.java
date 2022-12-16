package x590.javaclass.instruction.constant;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.AConstNullOperation;

public class AConstNullInstruction extends Instruction {
	
	public static final AConstNullInstruction INSTANCE = new AConstNullInstruction();
	
	private AConstNullInstruction() {}
	
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return AConstNullOperation.INSTANCE;
	}
}