package x590.javaclass.type;

public abstract class TwoWayCastSpecialType extends SpecialType {
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return castToNarrowestImpl(other);
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return castToWidestImpl(other);
	}
}