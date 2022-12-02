package x590.javaclass.type;

public class DoubleType extends PrimitiveType {
	
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