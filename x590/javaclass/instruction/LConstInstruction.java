package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.LConstOperation;
import x590.javaclass.operation.Operation;

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