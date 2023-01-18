package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.io.ExtendedDataInputStream;

public final class InvokeDynamicConstant extends Constant {
	
	public final int bootstrapMethodAttrIndex;
	public final int nameAndTypeIndex;
	private NameAndTypeConstant nameAndType;
	
	public InvokeDynamicConstant(ExtendedDataInputStream in) {
		bootstrapMethodAttrIndex = in.readUnsignedShort();
		nameAndTypeIndex = in.readUnsignedShort();
	}
	
	@Override
	void init(ConstantPool pool) {
		nameAndType = pool.get(nameAndTypeIndex);
	}
	
	public NameAndTypeConstant getNameAndType() {
		return nameAndType;
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(18);
		out.writeShort(bootstrapMethodAttrIndex);
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
