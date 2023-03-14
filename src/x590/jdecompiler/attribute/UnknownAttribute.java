package x590.jdecompiler.attribute;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;

public final class UnknownAttribute extends Attribute {
	
	private final byte[] data;
	
	protected UnknownAttribute(String name, int length, ExtendedDataInputStream in) {
		super(name, length);
		this.data = new byte[length];
		in.readFully(data);
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		serializeHeader(out);
		out.write(data);
	}
}
