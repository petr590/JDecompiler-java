package x590.jdecompiler.instruction.other;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.TypeSize;
import x590.util.annotation.Nullable;

public class SwapInstruction implements Instruction {
	
	@Override
	public @Nullable Operation toOperation(DecompilationContext context) {
		Operation value1 = context.popWithSize(TypeSize.WORD);
		Operation value2 = context.popWithSize(TypeSize.WORD);
		
		context.push(value1);
		context.push(value2);
		
		return null;
	}
}
