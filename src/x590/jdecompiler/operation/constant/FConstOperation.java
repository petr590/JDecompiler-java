package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.FloatConstant;

public final class FConstOperation extends IntConvertibleConstOperation<FloatConstant> {
	
	public FConstOperation(FloatConstant constant) {
		super(constant);
	}
	
	public float getValue() {
		return constant.getValue();
	}
}
