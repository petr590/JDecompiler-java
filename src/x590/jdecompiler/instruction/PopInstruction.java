package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.PopOperation;
import x590.jdecompiler.type.TypeSize;

public class PopInstruction extends Instruction {
	
	private final TypeSize size;
	
	public PopInstruction(TypeSize size) {
		this.size = size;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PopOperation(size, context);
	}
}
