package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.MethodTypeConstOperation;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class MethodTypeConstant extends ConstValueConstant {

	public final int descriptorIndex;
	private Utf8Constant descriptor;
	
	protected MethodTypeConstant(ExtendedDataInputStream in) {
		descriptorIndex = in.readUnsignedShort();
	}
	
	public MethodTypeConstant(Utf8Constant descriptor) {
		descriptorIndex = 0;
		this.descriptor = descriptor;
	}
	
	@Override
	void init(ConstantPool pool) {
		descriptor = pool.get(descriptorIndex);
	}
	
	public Utf8Constant getDescriptor() {
		return descriptor;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "#MethodType#";
	}
	
	@Override
	public Type getType() {
		return ClassType.METHOD_TYPE;
	}
	
	public String getConstantName() {
		return "MethodType";
	}
	
	@Override
	public Operation toOperation() {
		return new MethodTypeConstOperation(this);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(16);
		out.writeByte(descriptorIndex);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof MethodTypeConstant constant && this.equals(constant);
	}
	
	public boolean equals(MethodTypeConstant other) {
		return this == other || this.descriptor.equals(other.descriptor);
	}
}