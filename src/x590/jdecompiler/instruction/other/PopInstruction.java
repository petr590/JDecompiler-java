package x590.jdecompiler.instruction.other;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.other.PopOperation;
import x590.jdecompiler.type.TypeSize;

public class PopInstruction implements Instruction {
	
	private final TypeSize size;
	
	public PopInstruction(TypeSize size) {
		this.size = size;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PopOperation(size, context);
	}
}
