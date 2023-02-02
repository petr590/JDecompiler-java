package x590.jdecompiler.operation;

import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public abstract class BooleanOperation extends Operation {
	
	@Override
	public final Type getReturnType() {
		return PrimitiveType.BOOLEAN;
	}
}
