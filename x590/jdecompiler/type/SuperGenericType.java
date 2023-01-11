package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.io.StringifyOutputStream;

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
