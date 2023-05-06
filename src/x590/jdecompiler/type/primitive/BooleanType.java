package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.reference.ClassType;

public final class BooleanType extends PrimitiveType {
	
	public static final BooleanType INSTANCE = new BooleanType();
	
	private BooleanType() {
		super("Z", "boolean", "bool");
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.BOOLEAN;
	}
}
