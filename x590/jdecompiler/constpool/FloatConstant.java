package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.FConstOperation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;

public class FloatConstant extends ConstValueConstant {
	
	private final float value;
	
	protected FloatConstant(ExtendedDataInputStream in) {
		this.value = in.readFloat();
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return StringUtil.toLiteral(value);
	}
	
	public float getValue() {
		return value;
	}
	
	@Override
	public Type getType() {
		return PrimitiveType.FLOAT;
	}
	
	@Override
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
