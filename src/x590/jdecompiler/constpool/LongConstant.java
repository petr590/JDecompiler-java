package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.constant.LConstOperation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;

public final class LongConstant extends ConstValueConstant {
	
	private final long value;
	
	protected LongConstant(ExtendedDataInputStream in) {
		this.value = in.readLong();
	}
	
	public long getValue() {
		return value;
	}
	
	@Override
	protected boolean holdsTwo() {
		return true;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return StringUtil.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.LONG;
	}
	
	@Override
	public String getConstantName() {
		return "Long";
	}
	
	@Override
	public Operation toOperation() {
		return new LConstOperation(value);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(5);
		out.writeLong(value);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof LongConstant constant && this.equals(constant);
	}
	
	public boolean equals(LongConstant other) {
		return this == other || value == other.value;
	}
}
