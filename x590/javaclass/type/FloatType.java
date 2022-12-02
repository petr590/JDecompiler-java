package x590.javaclass.type;

public class FloatType extends PrimitiveType {
	
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
}