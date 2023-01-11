package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.TypeSize;

public class LdcInstruction extends InstructionWithIndex {
	
	private final TypeSize size;
	
	public LdcInstruction(TypeSize size, DisassemblerContext context, int index) {
		super(index);
		this.size = size;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return context.pool.<ConstValueConstant>get(index).toOperation(size);
	}
}
