package x590.jdecompiler.constpool;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;

public class MethodrefConstant extends ReferenceConstant {
	
	protected MethodrefConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	public MethodrefConstant(int classIndex, int nameAndTypeIndex, ConstantPool pool) {
		super(classIndex, nameAndTypeIndex, pool);
	}
	
	@Override
	public String getConstantName() {
		return "Methodref";
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.writeByte(TAG_METHODREF);
		out.writeShort(getClassIndex());
		out.writeShort(getNameAndTypeIndex());
	}
}
