package x590.javaclass.type;

public abstract class GenericType extends ReferenceType {
	
	@Override
	public String getNameForVariable() {
		throw new UnsupportedOperationException("Seriously? Variable of unknown generic type?");
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return other.isBasicReferenceType();
	}
}