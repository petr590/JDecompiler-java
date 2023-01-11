package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.IConstOperation;

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
