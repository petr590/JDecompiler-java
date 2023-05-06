package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;

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
	protected boolean canCastToNarrowest(Type other) {
		return this == other || other.isReferenceType();
	}
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		if(this == other)
			return this;
		
		if(other.isReferenceType())
			return kind.isNarrowest() ?
					UncertainReferenceType.getInstance((ReferenceType)other) :
					UncertainReferenceType.getInstance(ClassType.OBJECT, (ReferenceType)other, kind);
		
		if(other.isUncertainReferenceType())
			return other;
		
		return null;
	}
	
	@Override
	protected Type reversedCastImpl(Type other, CastingKind kind) {
		return this == other || other.isAnyReferenceType() ? other : null;
	}
	
	@Override
	public BasicType reduced() {
		return ClassType.OBJECT;
	}
}
