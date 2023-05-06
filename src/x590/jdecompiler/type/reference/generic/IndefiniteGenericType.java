package x590.jdecompiler.type.reference.generic;

import java.util.List;

import x590.jdecompiler.type.reference.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public abstract class IndefiniteGenericType extends GenericType {
	
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
