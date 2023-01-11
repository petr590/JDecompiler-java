package x590.jdecompiler.type;

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
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	public boolean isImplicitSubtypeOf(Type other) {
		return this == other || other == PrimitiveType.DOUBLE;
	}
}
