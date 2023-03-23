package x590.jdecompiler.operation;

import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public interface BooleanOperation extends Operation {
	
	@Override
	public default Type getReturnType() {
		return PrimitiveType.BOOLEAN;
	}
}
