package x590.jdecompiler.attribute.signature;

import x590.jdecompiler.attribute.Attribute;

public abstract class SignatureAttribute extends Attribute {
	
	public SignatureAttribute(int nameIndex, String name, int length) {
		super(nameIndex, name, length);
	}
}
