package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.FConstOperation;

public class FConstInstruction extends ConstInstruction {
	
	private final float value;
	
	public FConstInstruction(float value) {
		this.value = value;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FConstOperation(value);
	}
}
