package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedStringReader;

public abstract class BoundedGenericType extends GenericType {
	
	public final ReferenceType type;
	
	public BoundedGenericType(ExtendedStringReader in) {
		this.type = parseSignatureParameter(in);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		type.addImports(classinfo);
	}
}
