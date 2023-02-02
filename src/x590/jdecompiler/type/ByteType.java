package x590.jdecompiler.type;

public final class ByteType extends IntegralType {
	
	public static final ByteType INSTANCE = new ByteType();
	
	private ByteType() {
		super("B", "byte", "b");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.BYTE;
	}
	
	@Override
	public int getCapacity() {
		return 1;
	}
	
	@Override
	public Type toUncertainIntegralType() {
		return PrimitiveType.BYTE_SHORT_INT_CHAR;
	}
}
