package x590.javaclass.type;

import x590.javaclass.ClassInfo;

public class AnyType extends SpecialType {
	
	public static final AnyType INSTANCE = new AnyType();
	
	private AnyType() {}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return ClassType.OBJECT.toString(classinfo);
	}
	
	@Override
	public String toString() {
		return "AnyType";
	}
	
	@Override
	public final String getEncodedName() {
		return "SAnyType";
	}
	
	@Override
	public final String getName() {
		return "java.lang.Object";
	}
	
	@Override
	public final String getNameForVariable() {
		return "o";
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES; // ???
	}
	
	@Override
	protected boolean isSubtypeOfImpl(Type other) {
		return true;
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return other;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return other.isPrimitive() ? ((PrimitiveType)other).toVariableCapacityIntegralType() : other;
	}
}