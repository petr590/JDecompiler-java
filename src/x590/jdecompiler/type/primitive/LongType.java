package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.reference.ClassType;

public final class LongType extends PrimitiveType {
	
	public static final LongType INSTANCE = new LongType();
	
	private LongType() {
		super("J", "long", "l");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.LONG;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.LONG;
	}
	
	@Override
	public boolean isImplicitSubtypeOf(Type other) {
		return this == other || other == PrimitiveType.FLOAT || other == PrimitiveType.DOUBLE;
	}
}
