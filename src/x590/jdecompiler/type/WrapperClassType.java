package x590.jdecompiler.type;

public final class WrapperClassType extends ClassType {
	
	private final PrimitiveType primitiveType;
	
	public WrapperClassType(String in, Class<?> thisClass, PrimitiveType primitiveType) {
		super(in, thisClass);
		this.primitiveType = primitiveType;
	}
	
	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}
}
