package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.FloatConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.FConstOperation;

public class FConstInstruction extends ConstInstruction<FloatConstant> {
	
	public FConstInstruction(FloatConstant constant) {
		super(constant);
	}
	
	public FConstInstruction(float value) {
		super(ConstantPool.findOrCreateConstant(value));
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new FConstOperation(constant);
	}
}
