package x590.jdecompiler.type;

public final class DoubleType extends PrimitiveType {
	
	public static final DoubleType INSTANCE = new DoubleType();
	
	private DoubleType() {
		super("D", "double", "d");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.DOUBLE;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.EIGHT_BYTES;
	}
}
