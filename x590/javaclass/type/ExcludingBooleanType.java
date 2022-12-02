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
		return "SExcludingBooleanType";
	}
	
	@Override
	public String getName() {
		return "ExcludingBooleanType";
	}
	
	@Override
	public String getNameForVariable() {
		return "e";
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	protected boolean isSubtypeOfImpl(Type other) {
		return castToNarrowest(other) != null;
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		if(other instanceof VariableCapacityIntegralType intergalType) {
			return !intergalType.includeBoolean ? intergalType :
					VariableCapacityIntegralType.getInstance(intergalType.minCapacity, intergalType.maxCapacity, false, intergalType.includeChar);
		}
		
		return other != PrimitiveType.BOOLEAN ? other : null;
	}

	@Override
	protected Type castToWidestImpl(Type other) {
		return castToNarrowest(other);
	}
}