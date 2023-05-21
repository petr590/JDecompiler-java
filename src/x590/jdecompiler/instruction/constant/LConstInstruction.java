package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.LongConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.LConstOperation;

public class LConstInstruction extends ConstInstruction<LongConstant> {
	
	public LConstInstruction(LongConstant constant) {
		super(constant);
	}
	
	public LConstInstruction(long value) {
		super(ConstantPool.findOrCreateConstant(value));
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LConstOperation(constant);
	}
}
