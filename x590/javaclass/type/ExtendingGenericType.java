package x590.javaclass.type;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedStringReader;

public class ExtendingGenericType extends BoundedGenericType {
	
	public ExtendingGenericType(ExtendedStringReader in) {
		super(in);
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "? extends " + type.toString(classinfo);
	}
	
	@Override
	public String toString() {
		return "ExtendingGenericType(" + type.toString() + ")";
	}
}