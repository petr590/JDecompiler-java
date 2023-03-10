package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;

public final class AnyGenericType extends GenericType {
	
	public static final AnyGenericType INSTANCE = new AnyGenericType();
	
	private AnyGenericType() {}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.write("?");
	}
	
	@Override
	public String toString() {
		return "AnyGenericType";
	}
}
