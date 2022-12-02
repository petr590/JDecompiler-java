package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DConstOperation;
import x590.javaclass.operation.Operation;

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