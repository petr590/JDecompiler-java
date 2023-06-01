package x590.jdecompiler.type.reference.generic;

import java.lang.reflect.WildcardType;
import java.util.List;

import x590.jdecompiler.type.reference.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public abstract class IndefiniteGenericType extends GenericType {
	
	public static ReferenceType fromWildcardType(WildcardType wildcardType) {
		var upperBounds = wildcardType.getUpperBounds();
		var lowerBounds = wildcardType.getLowerBounds();
		
		if(upperBounds.length > 1 || lowerBounds.length > 1) {
			throw new IllegalArgumentException("wildcardType: " + wildcardType);
		}
		
		if(lowerBounds.length == 0) {
			return upperBounds.length == 1 && upperBounds[0] == Object.class ?
					AnyGenericType.INSTANCE :
					new SuperGenericType(ReferenceType.fromReflectType(upperBounds[0]));
		} else {
			return new ExtendingGenericType(ReferenceType.fromReflectType(lowerBounds[0]));
		}
	}
	
	
	@Override
	public @Nullable ReferenceType getSuperType() {
		return null;
	}
	
	@Override
	public @Nullable @Immutable List<? extends ReferenceType> getInterfaces() {
		return null;
	}

	@Override
	public String getNameForVariable() {
		throw new UnsupportedOperationException("Seriously? Variable of unknown generic type?");
	}
}
