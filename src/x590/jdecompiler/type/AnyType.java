package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;

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
	protected boolean canCastTo(Type other) {
		return true;
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return other;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return other instanceof PrimitiveType primitiveType ? primitiveType.toUncertainIntegralType() : other;
	}
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return castToNarrowestImpl(other);
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return castToWidestImpl(other);
	}
}
