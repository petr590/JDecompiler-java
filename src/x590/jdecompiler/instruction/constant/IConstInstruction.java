package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.IntegerConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.IConstOperation;

public class IConstInstruction extends ConstInstruction<IntegerConstant> {
	
	public IConstInstruction(IntegerConstant constant) {
		super(constant);
	}
	
	public IConstInstruction(int value) {
		super(ConstantPool.findOrCreateConstant(value));
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new IConstOperation(constant);
	}
}
