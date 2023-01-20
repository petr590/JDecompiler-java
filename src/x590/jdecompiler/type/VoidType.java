package x590.jdecompiler.type;

public final class VoidType extends PrimitiveType {
	
	public static final VoidType INSTANCE = new VoidType();
	
	private VoidType() {
		super("V", "void", "v");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.VOID;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.VOID;
	}
}
