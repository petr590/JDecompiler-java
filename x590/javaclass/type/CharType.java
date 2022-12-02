package x590.javaclass.type;

public class CharType extends PrimitiveType {
	
	public static final CharType INSTANCE = new CharType();
	
	private CharType() {
		super("C", "char", "ch");
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.CHARACTER;
	}
	
	@Override
	public Type toVariableCapacityIntegralType() {
		return PrimitiveType.CHAR_OR_INT;
	}
}