package x590.jdecompiler.operation.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class ILoadOperation extends LoadOperation {
	
	public ILoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN, context, index);
	}
}
