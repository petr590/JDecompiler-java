package x590.jdecompiler.constpool;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;

public final class FieldrefConstant extends ReferenceConstant {
	
	protected FieldrefConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	public FieldrefConstant(int classIndex, int nameAndTypeIndex, ConstantPool pool) {
		super(classIndex, nameAndTypeIndex, pool);
	}
	
	@Override
	public String getConstantName() {
		return "Fieldref";
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.writeByte(TAG_FIELDREF);
		out.writeShort(getClassIndex());
		out.writeShort(getNameAndTypeIndex());
	}
}
