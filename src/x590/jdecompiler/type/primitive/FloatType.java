package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.reference.ClassType;

public final class FloatType extends PrimitiveType {
	
	public static final FloatType INSTANCE = new FloatType();
	
	private FloatType() {
		super("F", "float", "f");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.FLOAT;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	public boolean canImplicitCastToNarrowest(Type other) {
		return this == other || other == PrimitiveType.DOUBLE;
	}
}
