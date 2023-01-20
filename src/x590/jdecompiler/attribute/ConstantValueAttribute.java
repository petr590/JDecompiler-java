package x590.jdecompiler.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.Type;

public final class ConstantValueAttribute extends Attribute implements StringWritable {
	
	public final int valueIndex;
	public final ConstValueConstant value;
	
	protected ConstantValueAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		this.valueIndex = in.readUnsignedShort();
		this.value = pool.get(valueIndex);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(value, classinfo);
	}
	
	public void writeAs(StringifyOutputStream out, ClassInfo classinfo, Type type) {
		out.write(value.toStringAs(type, classinfo));
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeShort(valueIndex);
	}
}
