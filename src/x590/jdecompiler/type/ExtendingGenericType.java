package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;

public final class ExtendingGenericType extends BoundedGenericType {
	
	public ExtendingGenericType(ExtendedStringInputStream in) {
		super(in);
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.print("? extends ").printObject(type, classinfo);
	}
	
	@Override
	public String toString() {
		return "ExtendingGenericType(" + type.toString() + ")";
	}
}
