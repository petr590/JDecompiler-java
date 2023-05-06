package x590.jdecompiler.type.reference.generic;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ReferenceType;

/** Описывает дженерик, ограниченный только сверху или снизу или вообще не ограниченный */
public abstract class GenericType extends ReferenceType {
	
	@Override
	protected boolean canCastToNarrowest(Type other) {
		return other.isReferenceType();
	}
	
	@Override
	public boolean isGenericType() {
		return true;
	}
}
