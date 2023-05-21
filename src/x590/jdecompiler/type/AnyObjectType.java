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
	public boolean isDefinitelySubtypeOf(Type other) {
		return this == other || other == Types.ANY_TYPE;
	}
	
	@Override
	protected boolean canCastToNarrowestImpl(Type other) {
		return other.isAnyReferenceType();
	}
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		if(this == other)
			return this;
		
		if(other instanceof ReferenceType referenceType)
			return kind.isNarrowest() ?
					UncertainReferenceType.getInstance(referenceType) :
					UncertainReferenceType.getInstance(ClassType.OBJECT, referenceType, kind);
		
		if(other.isAnyReferenceType())
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
