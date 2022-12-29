package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.io.ExtendedDataInputStream;

public class NameAndTypeConstant extends Constant {
	
	public final int nameIndex, descriptorIndex;
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
		out.writeByte(12);
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