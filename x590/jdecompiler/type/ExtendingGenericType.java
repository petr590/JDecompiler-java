package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.io.StringifyOutputStream;

public final class ExtendingGenericType extends BoundedGenericType {
	
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
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print("? extends ").print(type, classinfo);
	}
}
