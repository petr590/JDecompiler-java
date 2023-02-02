package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.LongConstant;

public final class LConstOperation extends IntConvertibleConstOperation<LongConstant> {
	
	public LConstOperation(LongConstant constant) {
		super(constant);
	}
	
	public long getValue() {
		return constant.getValue();
	}
}
