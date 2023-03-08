package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.LConstOperation;
import x590.util.annotation.Nullable;

public class LPushInstruction implements Instruction {
	
	protected final long value;
	
	public LPushInstruction(long value) {
		this.value = value;
	}

	@Override
	public @Nullable Operation toOperation(DecompilationContext context) {
		return new LConstOperation(context.getClassinfo().getConstPool().findOrCreateConstant(value));
	}
}
