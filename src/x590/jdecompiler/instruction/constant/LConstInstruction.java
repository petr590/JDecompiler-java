package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.constant.LConstOperation;

public class LConstInstruction extends ConstInstruction {
	
	private final long value;
	
	public LConstInstruction(long value) {
		this.value = value;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LConstOperation(value);
	}
}
