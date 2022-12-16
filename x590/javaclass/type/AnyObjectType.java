package x590.javaclass.type;

import x590.javaclass.ClassInfo;

public final class AnyObjectType extends TwoWayCastSpecialType {
	
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
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(ClassType.OBJECT);
	}
	
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return this == other || other.isReferenceType();
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.canCastTo(other) ? other : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastTo(other) ? this : null;
	}
}