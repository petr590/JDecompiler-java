package x590.jdecompiler.type.reference.generic;

import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.util.annotation.Nullable;

/** Дженерик, ограниченный снизу */
public final class SuperGenericType extends BoundedGenericType {
	
	public SuperGenericType(ExtendedStringInputStream in) {
		super(in);
	}
	
	@Override
	public @Nullable ReferenceType getSuperType() {
		return ClassType.OBJECT;
	}
	
	@Override
	protected String encodedBound() {
		return "-";
	}
	
	@Override
	protected String bound() {
		return "super";
	}
	
	@Override
	public String toString() {
		return "? super" + getType().toString();
	}
}
