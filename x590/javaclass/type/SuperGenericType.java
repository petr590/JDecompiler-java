package x590.javaclass.type;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedStringReader;

public class SuperGenericType extends DefinedGenericType {
	
	public SuperGenericType(ExtendedStringReader in) {
		super(in);
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "? super " + type.toString(classinfo);
	}
	
	@Override
	public String toString() {
		return "SuperGenericType(" + type.toString() + ")";
	}
}