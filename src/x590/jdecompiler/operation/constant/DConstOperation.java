package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.DoubleConstant;

public final class DConstOperation extends IntConvertibleConstOperation<DoubleConstant> {
	
	public DConstOperation(DoubleConstant value) {
		super(value);
	}
	
	public double getValue() {
		return constant.getValue();
	}
}
