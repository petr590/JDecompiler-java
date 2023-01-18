package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.constant.StringConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;

public final class StringConstant extends ConstValueConstant {
	
	private final int index;
	private Utf8Constant value;
	
	public StringConstant(ExtendedDataInputStream in) {
		index = in.readUnsignedShort();
	}
	
	@Override
	void init(ConstantPool pool) {
		value = pool.get(index);
	}
	
	public Utf8Constant getUtf8Constant() {
		return value;
	}
	
	public String getString() {
		return value.getString();
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return StringUtil.toLiteral(value.getString());
	}
	
	@Override
	public Type getType() {
		return ClassType.STRING;
	}
	
	@Override
	public String getConstantName() {
		return "String";
	}
	
	@Override
	public Operation toOperation() {
		return new StringConstOperation(value.getString());
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(8);
		out.writeShort(index);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof StringConstant constant && this.equals(constant);
	}
	
	public boolean equals(StringConstant other) {
		return this == other || this.value.equals(other.value);
	}
}
