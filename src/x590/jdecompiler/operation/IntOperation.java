package x590.jdecompiler.operation;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;

public interface IntOperation extends Operation {
	
	@Override
	default Type getReturnType() {
		return PrimitiveType.INT;
	}
}
