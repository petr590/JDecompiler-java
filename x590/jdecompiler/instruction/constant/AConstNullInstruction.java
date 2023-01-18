package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.constant.AConstNullOperation;

public class AConstNullInstruction extends Instruction {
	
	public static final AConstNullInstruction INSTANCE = new AConstNullInstruction();
	
	private AConstNullInstruction() {}
	
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return AConstNullOperation.INSTANCE;
	}
}
