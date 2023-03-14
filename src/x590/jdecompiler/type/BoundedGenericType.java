package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedStringInputStream;

public abstract class BoundedGenericType extends GenericType {
	
	public final ReferenceType type;
	
	public BoundedGenericType(ExtendedStringInputStream in) {
		this.type = parseSignatureParameter(in);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		type.addImports(classinfo);
	}
}
