package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.FConstOperation;
import x590.util.annotation.Nullable;

public class FPushInstruction implements Instruction {
	
	protected final float value;
	
	public FPushInstruction(float value) {
		this.value = value;
	}

	@Override
	public @Nullable Operation toOperation(DecompilationContext context) {
		return new FConstOperation(context.classinfo.getConstPool().findOrCreateConstant(value));
	}
}
