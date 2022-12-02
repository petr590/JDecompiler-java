package x590.javaclass.type;

import x590.javaclass.ClassInfo;

public final class AnyObjectType extends SpecialType {
	
	public static final AnyObjectType INSTANCE = new AnyObjectType();
	
	private AnyObjectType() {}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return ClassType.OBJECT.toString(classinfo);
	}
	
	@Override
	public String toString() {
		return "AnyObjectType";
	}
	
	@Override
	public final String getEncodedName() {
		return "SAnyObjectType";
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
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	protected boolean isSubtypeOfImpl(Type other) {
		return this == other || (other.isBasic() && !other.isPrimitive());
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.isSubtypeOf(other) ? other : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.isSubtypeOf(other) ? this : null;
	}
}