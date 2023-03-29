package x590.jdecompiler.type;

import x590.jdecompiler.io.ExtendedStringInputStream;

/** Дженерик, ограниченный сверху */
public final class ExtendingGenericType extends BoundedGenericType {
	
	public ExtendingGenericType(ExtendedStringInputStream in) {
		super(in);
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
		return "ExtendingGenericType(" + getType().toString() + ")";
	}
}
