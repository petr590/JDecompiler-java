package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.DConstOperation;
import x590.util.annotation.Nullable;

public class DPushInstruction implements Instruction {
	
	protected final double value;
	
	public DPushInstruction(double value) {
		this.value = value;
	}

	@Override
	public @Nullable Operation toOperation(DecompilationContext context) {
		return new DConstOperation(ConstantPool.findOrCreateConstant(value));
	}
}
