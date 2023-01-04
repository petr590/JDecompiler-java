package x590.javaclass.type;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.io.StringifyOutputStream;

public class SuperGenericType extends BoundedGenericType {
	
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
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print("? super ").print(type, classinfo);
	}
}
