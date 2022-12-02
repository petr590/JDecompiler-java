package x590.javaclass.type;

import x590.javaclass.io.ExtendedStringReader;

public abstract class DefinedGenericType extends GenericType {
	
	public final ReferenceType type;
	
	public DefinedGenericType(ExtendedStringReader in) {
		this.type = parseParameter(in);
	}
}