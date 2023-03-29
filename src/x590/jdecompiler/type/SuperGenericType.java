package x590.jdecompiler.type;

import x590.jdecompiler.io.ExtendedStringInputStream;

/** Дженерик, ограниченный снизу */
public final class SuperGenericType extends BoundedGenericType {
	
	public SuperGenericType(ExtendedStringInputStream in) {
		super(in);
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
		return "SuperGenericType(" + getType().toString() + ")";
	}
}
