package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.DoubleConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.DConstOperation;

public class DConstInstruction extends ConstInstruction<DoubleConstant> {
	
	public DConstInstruction(DoubleConstant constant) {
		super(constant);
	}
	
	public DConstInstruction(double value) {
		super(ConstantPool.findOrCreateConstant(value));
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DConstOperation(constant);
	}
}
