package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.io.ExtendedDataInputStream;

public class InvokeDynamicConstant extends Constant {

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
}