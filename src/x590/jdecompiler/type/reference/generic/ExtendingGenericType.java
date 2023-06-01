package x590.jdecompiler.type.reference.generic;

import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.util.annotation.Nullable;

/** Дженерик, ограниченный сверху */
public final class ExtendingGenericType extends BoundedGenericType {
	
	public ExtendingGenericType(ReferenceType type) {
		super(type);
	}
	
	public ExtendingGenericType(ExtendedStringInputStream in) {
		super(in);
	}
	
	@Override
	public @Nullable ReferenceType getSuperType() {
		return getType();
	}
	
	@Override
	protected String encodedBound() {
		return "+";
	}
	
	@Override
	protected String bound() {
		return "extends";
	}
	
	@Override
	public String toString() {
		return "? extends " + getType().toString();
	}
}
