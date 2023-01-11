package x590.jdecompiler.type;

/** Описывает дженерик, ограниченный только сверху или снизу или вообще не ограниченный */
public abstract class GenericType extends ReferenceType {
	
	@Override
	public String getNameForVariable() {
		throw new UnsupportedOperationException("Seriously? Variable of unknown generic type?");
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return other.isBasicReferenceType();
	}
	
	@Override
	public boolean isGenericType() {
		return true;
	}
}
