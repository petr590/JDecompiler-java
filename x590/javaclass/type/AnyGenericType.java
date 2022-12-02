package x590.javaclass.type;

import x590.javaclass.ClassInfo;

public class AnyGenericType extends GenericType {
	
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