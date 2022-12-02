package x590.javaclass.type;

public class IntType extends IntegralType {
	
	public static final IntType INSTANCE = new IntType();
	
	private IntType() {
		super("I", "int", "n");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.INTEGER;
	}
	
	@Override
	public int getCapacity() {
		return 4;
	}
}