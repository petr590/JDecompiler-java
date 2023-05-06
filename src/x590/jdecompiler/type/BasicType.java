package x590.jdecompiler.type;

/**
 * Представляет Java тип (т.е. определённый тип, который существует в коде)
 */
public abstract class BasicType extends Type {
	
	@Override
	protected abstract Type castToNarrowestImpl(Type other);
	
	@Override
	protected abstract Type castToWidestImpl(Type other);
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		return kind.isNarrowest() ? castToNarrowestImpl(other) : castToWidestImpl(other);
	}
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return null;
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return null;
	}
	
	@Override
	protected Type reversedCastImpl(Type other, CastingKind kind) {
		return kind.isNarrowest() ? reversedCastToNarrowestImpl(other) : reversedCastToWidestImpl(other);
	}
	
	@Override
	public BasicType reduced() {
		return this;
	}
}
