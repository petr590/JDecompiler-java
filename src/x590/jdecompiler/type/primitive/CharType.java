package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.reference.ClassType;

public final class CharType extends PrimitiveType {
	
	public static final CharType INSTANCE = new CharType();
	
	private CharType() {
		super("C", "char", "c");
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.CHARACTER;
	}
	
	@Override
	public Type toUncertainIntegralType() {
		return PrimitiveType.INT_CHAR;
	}
	
	@Override
	public boolean canCastToNarrowestImpl(Type other) {
		return this == other ||
				other instanceof IntegralType integralType && integralType.getCapacity() > CHAR_CAPACITY;
	}
	
	@Override
	public boolean canImplicitCastToNarrowest(Type other) {
		return canCastToNarrowest(other) || other.isLongOrFloatOrDouble();
	}
}
