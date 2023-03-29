package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;

/** Дженерик, не ограниченный ни сверху, ни снизу */
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
	
	@Override
	public String getEncodedName() {
		return "*";
	}
	
	@Override
	public String getName() {
		return "?";
	}
}
