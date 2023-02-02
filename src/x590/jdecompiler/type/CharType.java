package x590.jdecompiler.type;

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
	protected boolean canCastTo(Type other) {
		return this == other || other.isIntegral() && ((IntegralType)other).getCapacity() > CHAR_CAPACITY;
	}
	
	@Override
	public boolean isImplicitSubtypeOf(Type other) {
		return canCastTo(other) || other.isPrimitive() && (other == PrimitiveType.INT || other.isLongOrFloatOrDouble());
	}
}
