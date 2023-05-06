package x590.jdecompiler.operation.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class LLoadOperation extends LoadOperation {
	
	public LLoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.LONG, context, index);
	}
}
