package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.io.ExtendedDataInputStream;

public class InterfaceMethodrefConstant extends MethodrefConstant {

	protected InterfaceMethodrefConstant(ExtendedDataInputStream in) {
		super(in);
	}
	
	public InterfaceMethodrefConstant(int classIndex, int nameAndTypeIndex, ConstantPool pool) {
		super(classIndex, nameAndTypeIndex, pool);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(11);
		out.writeShort(classIndex);
		out.writeShort(nameAndTypeIndex);
	}
}