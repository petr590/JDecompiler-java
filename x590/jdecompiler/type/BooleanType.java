package x590.jdecompiler.type;

public final class BooleanType extends PrimitiveType {
	
	public static final BooleanType INSTANCE = new BooleanType();
	
	private BooleanType() {
		super("Z", "boolean", "bool");
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.BOOLEAN;
	}
}
