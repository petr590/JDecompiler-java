package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.FConstOperation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class FloatConstant extends ConstValueConstant {
	
	public final float value;

	protected FloatConstant(ExtendedDataInputStream in) {
		this.value = in.readFloat();
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return Util.toLiteral(value);
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.FLOAT;
	}
	
	
	public String getConstantName() {
		return "Float";
	}
	
	@Override
	public Operation toOperation() {
		return new FConstOperation(value);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(4);
		out.writeFloat(value);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof FloatConstant constant && this.equals(constant);
	}
	
	public boolean equals(FloatConstant other) {
		return this == other || this.value == other.value;
	}
}