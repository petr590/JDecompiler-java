package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.io.ExtendedDataInputStream;

public final class FieldrefConstant extends ReferenceConstant {
	
	protected FieldrefConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	public FieldrefConstant(int classIndex, int nameAndTypeIndex, ConstantPool pool) {
		super(classIndex, nameAndTypeIndex, pool);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(9);
		out.writeShort(getClassIndex());
		out.writeShort(getNameAndTypeIndex());
	}
}
