package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;

public final class AnyType extends Type {
	
	public static final AnyType INSTANCE = new AnyType();
	
	private AnyType() {}
	
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printObject(ClassType.OBJECT, classinfo);
	}
	
	@Override
	public String toString() {
		return "AnyType";
	}
	
	@Override
	public final String getEncodedName() {
		return "AnyType";
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
		return TypeSize.WORD; // ???
	}
	
	
	@Override
	protected boolean canCastToNarrowest(Type other) {
		return true;
	}
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		return kind.isNarrowest() ? other :
				other instanceof PrimitiveType primitiveType ? primitiveType.toUncertainIntegralType() : other;
	}
	
	@Override
	protected Type reversedCastImpl(Type other, CastingKind kind) {
		return castImpl(other, kind);
	}
	
	@Override
	public BasicType reduced() {
		return ClassType.OBJECT;
	}
}
