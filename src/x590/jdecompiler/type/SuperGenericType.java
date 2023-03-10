package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.io.StringifyOutputStream;

public final class SuperGenericType extends BoundedGenericType {
	
	public SuperGenericType(ExtendedStringReader in) {
		super(in);
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.print("? super ").printObject(type, classinfo);
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
