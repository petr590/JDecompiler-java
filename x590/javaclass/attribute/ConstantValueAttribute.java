package x590.javaclass.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;

public class ConstantValueAttribute extends Attribute {
	
	public final int valueIndex;
	public final ConstValueConstant value;
	
	protected ConstantValueAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		this.valueIndex = in.readUnsignedShort();
		this.value = pool.get(valueIndex);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeShort(valueIndex);
	}
}
