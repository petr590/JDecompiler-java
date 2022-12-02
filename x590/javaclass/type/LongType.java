package x590.javaclass.type;

public class LongType extends PrimitiveType {
	
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
		return TypeSize.EIGHT_BYTES;
	}
}