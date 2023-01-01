package x590.javaclass.type;

import x590.javaclass.io.ExtendedStringReader;

public abstract class BoundedGenericType extends GenericType {
	
	public final ReferenceType type;
	
	public BoundedGenericType(ExtendedStringReader in) {
		this.type = parseSignatureParameter(in);
	}
}