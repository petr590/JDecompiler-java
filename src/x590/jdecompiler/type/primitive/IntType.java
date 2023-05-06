package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.reference.ClassType;

public final class IntType extends IntegralType {
	
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
