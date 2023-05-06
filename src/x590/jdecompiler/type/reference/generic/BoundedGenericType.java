package x590.jdecompiler.type.reference.generic;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.reference.ReferenceType;

/** Дженерик, ограниченный сверху или снизу */
public abstract class BoundedGenericType extends IndefiniteGenericType {
	
	private final ReferenceType type;
	private final String encodedName, name;
	
	public BoundedGenericType(ExtendedStringInputStream in) {
		this.type = parseSignatureParameter(in);
		this.name = encodedBound() + type.getName();
		this.encodedName = "? " + bound() + type.getEncodedName();
	}
	
	public ReferenceType getType() {
		return type;
	}
	
	protected abstract String encodedBound();
	
	protected abstract String bound();
	
	@Override
	public void addImports(ClassInfo classinfo) {
		type.addImports(classinfo);
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printsp('?').printsp(bound()).printObject(type, classinfo);
	}
	
	@Override
	public String getEncodedName() {
		return encodedName;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
