package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;

public final class IntegerConstant extends ConstValueConstant {
	
	private final int value;
	
	protected IntegerConstant(ExtendedDataInputStream in) {
		this.value = in.readInt();
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return StringUtil.toLiteral(value);
	}
	
	@Override
	public String toStringAs(Type type, ClassInfo classinfo) {
		if(type.isSubtypeOf(PrimitiveType.BOOLEAN)) return StringUtil.toLiteral(value != 0);
		if(type.isSubtypeOf(PrimitiveType.BYTE)) return StringUtil.toLiteral((byte)value);
		if(type.isSubtypeOf(PrimitiveType.CHAR)) return StringUtil.toLiteral((char)value);
		if(type.isSubtypeOf(PrimitiveType.SHORT)) return StringUtil.toLiteral((short)value);
		return StringUtil.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN;
	}
	
	@Override
	public String getConstantName() {
		return "Integer";
	}
	
	@Override
	public Operation toOperation() {
		return new IConstOperation(value);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(3);
		out.writeInt(value);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof IntegerConstant constant && this.equals(constant);
	}
	
	public boolean equals(IntegerConstant other) {
		return this == other || this.value == other.value;
	}
}
