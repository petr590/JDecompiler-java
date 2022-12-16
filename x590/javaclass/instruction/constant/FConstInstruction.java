package x590.javaclass.instruction.constant;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.FConstOperation;

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