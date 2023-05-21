package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;

public final class ByteType extends IntegralType {
	
	public static final ByteType INSTANCE = new ByteType();
	
	private ByteType() {
		super("B", "byte", "b");
	}
	
	@Override
	public ClassType getWrapperType() {
		return ClassType.BYTE;
	}
	
	@Override
	public int getCapacity() {
		return BYTE_CAPACITY;
	}
	
	@Override
	public Type toUncertainIntegralType() {
		return BYTE_SHORT_INT_CHAR;
	}
}
