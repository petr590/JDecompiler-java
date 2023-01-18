package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;

public final class AnyGenericType extends GenericType {
	
	public static final AnyGenericType INSTANCE = new AnyGenericType();
	
	private AnyGenericType() {}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "?";
	}
	
	@Override
	public String toString() {
		return "AnyGenericType";
	}
}
