package x590.javaclass.type;

import x590.javaclass.ClassInfo;

public class ExcludingBooleanType extends SpecialType {
	
	public static final ExcludingBooleanType INSTANCE = new ExcludingBooleanType();
	
	private ExcludingBooleanType() {}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "ExcludingBooleanType";
	}
	
	@Override
	public String toString() {
		return "ExcludingBooleanType";
	}
	
	@Override
	public String getEncodedName() {
		return "ExcludingBooleanType";
	}
	
	@Override
	public String getName() {
		return "ExcludingBooleanType";
	}
	
	@Override
	public String getNameForVariable() {
		return "n";
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return other != PrimitiveType.BOOLEAN;
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		if(other instanceof UncertainIntegralType intergalType) {
			return !intergalType.includeBoolean ? intergalType :
					UncertainIntegralType.getInstance(intergalType.minCapacity, intergalType.maxCapacity, false, intergalType.includeChar);
		}
		
		return other != PrimitiveType.BOOLEAN ? other : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return castToNarrowestImpl(other);
	}
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return castToNarrowestImpl(other);
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return castToNarrowestImpl(other);
	}
}