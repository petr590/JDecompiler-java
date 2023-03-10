package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.attribute.AttributeNames;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.BootstrapMethodsAttribute;
import x590.jdecompiler.attribute.BootstrapMethodsAttribute.BootstrapMethod;
import x590.jdecompiler.io.ExtendedDataInputStream;

public final class InvokeDynamicConstant extends Constant {
	
	private final int bootstrapMethodIndex;
	private final int nameAndTypeIndex;
	private NameAndTypeConstant nameAndType;
	
	public InvokeDynamicConstant(ExtendedDataInputStream in) {
		bootstrapMethodIndex = in.readUnsignedShort();
		nameAndTypeIndex = in.readUnsignedShort();
	}
	
	@Override
	void init(ConstantPool pool) {
		nameAndType = pool.get(nameAndTypeIndex);
	}
	
	public BootstrapMethod getBootstrapMethod(Attributes attributes) {
		return attributes.<BootstrapMethodsAttribute>get(AttributeNames.BOOTSTRAP_METHODS).getBootstrapMethod(bootstrapMethodIndex);
	}
	
	public NameAndTypeConstant getNameAndType() {
		return nameAndType;
	}
	
	@Override
	public String getConstantName() {
		return "MethodType";
	}
	
	@Override
	public String toString() {
		return String.format("MethodTypeConstant { bootstrapMethodIndex = %d, nameAndType = %s }", bootstrapMethodIndex, nameAndType);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(0x12);
		out.writeShort(bootstrapMethodIndex);
		out.writeShort(nameAndTypeIndex);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof InvokeDynamicConstant constant && this.equals(constant);
	}
	
	public boolean equals(InvokeDynamicConstant other) {
		return this == other || this.nameAndType.equals(other.nameAndType);
	}
}
