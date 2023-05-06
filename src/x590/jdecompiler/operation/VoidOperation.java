package x590.jdecompiler.operation;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;

public interface VoidOperation extends Operation {
	
	@Override
	public default Type getReturnType() {
		return PrimitiveType.VOID;
	}
}
