package x590.jdecompiler.constpool;

import x590.jdecompiler.io.ExtendedDataInputStream;

abstract class ConstantWithUtf8String extends Constant {
	
	final int valueIndex;
	String value;
	
	public ConstantWithUtf8String(ExtendedDataInputStream in) {
		this.valueIndex = in.readUnsignedShort();
	}
	
	@Override
	protected void init(ConstantPool pool) {
		this.value = pool.getUtf8String(valueIndex);
	}
	
	public String getString() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("%sConstant { %s }", getConstantName(), value);
	}
}
