package x590.javaclass.attribute;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.io.ExtendedDataInputStream;

public class UnknownAttribute extends Attribute {
	
	private final byte[] data;
	
	protected UnknownAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in) {
		super(nameIndex, name, length);
		this.data = new byte[length];
		in.readFully(data);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.write(data);
	}
}
