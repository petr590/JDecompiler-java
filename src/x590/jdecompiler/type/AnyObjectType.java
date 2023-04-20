package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;

public final class AnyObjectType extends Type {
	
	public static final AnyObjectType INSTANCE = new AnyObjectType();
	
	private AnyObjectType() {}
	
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printObject(ClassType.OBJECT, classinfo);
	}
	
	@Override
	public String toString() {
		return "AnyObjectType";
	}
	
	@Override
	public final String getEncodedName() {
		return "AnyObjectType";
	}
	
	@Override
	public final String getName() {
		return "java.lang.Object";
	}
	
	@Override
	public final String getNameForVariable() {
		return "obj";
	}
	
	
	@Override
	public final boolean isAnyReferenceType() {
		return true;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(ClassType.OBJECT);
	}
	
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return this == other || other.isReferenceType();
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		if(this == other)
			return this;
		
		if(other.isReferenceType())
			return UncertainReferenceType.getInstance((ReferenceType)other);
		
		if(other.isUncertainReferenceType())
			return other;
		
		return null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		if(this == other)
			return this;
		
		if(other.isReferenceType())
			return UncertainReferenceType.getInstance(ClassType.OBJECT, (ReferenceType)other, true);
		
		if(other.isUncertainReferenceType())
			return other;
		
		return null;
	}
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return this == other || other.isAnyReferenceType() ? other : null;
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return this == other || other.isAnyReferenceType() ? other : null;
	}
}
