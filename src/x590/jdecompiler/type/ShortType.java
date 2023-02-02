package x590.jdecompiler.type;

public final class ShortType extends IntegralType {
	
	public static final ShortType INSTANCE = new ShortType();
	
	private ShortType() {
		super("S", "short", "s");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.SHORT;
	}
	
	@Override
	public int getCapacity() {
		return 2;
	}
	
	@Override
	public Type toUncertainIntegralType() {
		return PrimitiveType.SHORT_INT;
	}
}
