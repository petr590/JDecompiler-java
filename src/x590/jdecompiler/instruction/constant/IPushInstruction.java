package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.util.annotation.Nullable;

public class IPushInstruction implements Instruction {
	
	protected final int value;
	
	public IPushInstruction(int value) {
		this.value = value;
	}

	@Override
	public @Nullable Operation toOperation(DecompilationContext context) {
		return new IConstOperation(ConstantPool.findOrCreateConstant(value));
	}
}
