package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.IConstOperation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class IntegerConstant extends ConstValueConstant {
	
	public final int value;

	protected IntegerConstant(ExtendedDataInputStream in) {
		this.value = in.readInt();
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return Util.toLiteral(value);
	}
	
	public String toStringAs(Type type, ClassInfo classinfo) {
		if(type.isSubtypeOf(PrimitiveType.BOOLEAN)) return Util.toLiteral(value != 0);
		if(type.isSubtypeOf(PrimitiveType.BYTE)) return Util.toLiteral((byte)value);
		if(type.isSubtypeOf(PrimitiveType.CHAR)) return Util.toLiteral((char)value);
		if(type.isSubtypeOf(PrimitiveType.SHORT)) return Util.toLiteral((short)value);
		return Util.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.BYTE_SHORT_CHAR_INT_BOOLEAN;
	}
	
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