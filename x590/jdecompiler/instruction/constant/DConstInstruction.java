package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.constant.DConstOperation;

public class DConstInstruction extends ConstInstruction {
	
	private final double value;
	
	public DConstInstruction(double value) {
		this.value = value;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DConstOperation(value);
	}
}
