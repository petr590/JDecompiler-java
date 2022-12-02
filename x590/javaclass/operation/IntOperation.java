package x590.javaclass.operation;

import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public abstract class IntOperation extends Operation {
	
	@Override
	public final Type getReturnType() {
		return PrimitiveType.INT;
	}
}