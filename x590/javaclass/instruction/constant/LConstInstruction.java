package x590.javaclass.instruction.constant;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.LConstOperation;

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