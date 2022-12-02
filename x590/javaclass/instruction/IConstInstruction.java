package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.IConstOperation;
import x590.javaclass.operation.Operation;

public class IConstInstruction extends ConstInstruction {
	
	private final int value;
	
	public IConstInstruction(int value) {
		this.value = value;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IConstOperation(value);
	}
}