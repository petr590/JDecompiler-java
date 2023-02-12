package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.io.ExtendedDataInputStream;

public final class NameAndTypeConstant extends Constant {
	
	private final int nameIndex, descriptorIndex;
	private Utf8Constant name, descriptor;
	
	public NameAndTypeConstant(ExtendedDataInputStream in) {
		this.nameIndex = in.readUnsignedShort();
		this.descriptorIndex = in.readUnsignedShort();
	}
	
	public NameAndTypeConstant(int nameIndex, int descriptorIndex, ConstantPool pool) {
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		init(pool);
	}
	
	public int getNameIndex() {
		return nameIndex;
	}
	
	public int getDescriptorIndex() {
		return descriptorIndex;
	}
	
	@Override
	void init(ConstantPool pool) {
		name = pool.get(nameIndex);
		descriptor = pool.get(descriptorIndex);
	}
	
	public Utf8Constant getNameConstant() {
		return name;
	}
	
	public Utf8Constant getDescriptor() {
		return descriptor;
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(0xC);
		out.writeShort(nameIndex);
		out.writeShort(descriptorIndex);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof NameAndTypeConstant constant && this.equals(constant);
	}
	
	public boolean equals(NameAndTypeConstant other) {
		return this == other || this.name.equals(other.name) && this.descriptor.equals(other.descriptor);
	}
}
