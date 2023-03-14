package x590.jdecompiler.attribute;

import x590.jdecompiler.Importable;
import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;

/**
 * Представляет атрибут в class файле.
 * Атрибуты может быть в классе, поле, методе, другом атрибуте
 */
public abstract class Attribute implements JavaSerializable, Importable {
	
	private final String name;
	private final int length;
	
	protected Attribute(String name, int length) {
		this.name = name;
		this.length = length;
	}
	
	
	protected static void checkLength(String name, int length, int requiredLength) {
		if(length != requiredLength)
			throw new DisassemblingException("Length of the \"" + name + "\" attribute must be " + requiredLength + ", got " + length);
	}
	
	
	public static Attribute read(ExtendedDataInputStream in, ConstantPool pool, Location location) {
		String name = pool.getUtf8String(in.readUnsignedShort());
		int length = in.readInt();
		int pos = in.available() - length;
		
		Attribute attribute = AttributeType.getAttributeType(location, name).readAttribute(name, length, in, pool);
		
		if(pos == in.available())
			return attribute;
		else
			throw new DisassemblingException("Attribute \"" + name + "\" was disassembled wrong: position difference " + (pos - in.available()));
	}
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		return length;
	}
	
	protected void serializeHeader(ExtendedDataOutputStream out) {
//		out.writeShort(nameIndex);
		out.writeInt(length);
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		throw new IllegalStateException("Not released yet :(");
	}
}
