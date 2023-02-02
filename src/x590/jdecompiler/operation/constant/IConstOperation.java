package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.IntegerConstant;

public final class IConstOperation extends ConstOperation<IntegerConstant> {
	
	public IConstOperation(IntegerConstant constant) {
		super(constant);
	}
	
	public int getValue() {
		return constant.getValue();
	}
}
