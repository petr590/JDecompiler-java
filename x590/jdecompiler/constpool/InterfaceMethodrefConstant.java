package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.io.ExtendedDataInputStream;

public final class InterfaceMethodrefConstant extends MethodrefConstant {
	
	protected InterfaceMethodrefConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	public InterfaceMethodrefConstant(int classIndex, int nameAndTypeIndex, ConstantPool pool) {
		super(classIndex, nameAndTypeIndex, pool);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(11);
		out.writeShort(getClassIndex());
		out.writeShort(getNameAndTypeIndex());
	}
}
