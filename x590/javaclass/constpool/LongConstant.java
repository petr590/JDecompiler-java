package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.LConstOperation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class LongConstant extends ConstValueConstant {
	
	public final long value;

	protected LongConstant(ExtendedDataInputStream in) {
		this.value = in.readLong();
	}
	
	@Override
	protected boolean holdsTwo() {
		return true;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return Util.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.LONG;
	}
	
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
		return this == other || this.value == other.value;
	}
}