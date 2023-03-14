package x590.jdecompiler.attribute;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.constpool.ConstableValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public final class ConstantValueAttribute extends Attribute {
	
	public final int valueIndex;
	public final ConstableValueConstant<?> value;
	
	protected ConstantValueAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		this.valueIndex = in.readUnsignedShort();
		this.value = pool.get(valueIndex);
	}
	
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo, Type type, @Nullable FieldDescriptor descriptor) {
		value.writeValue(out, classinfo, type, true, descriptor);
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		serializeHeader(out);
		out.writeShort(valueIndex);
	}
}
