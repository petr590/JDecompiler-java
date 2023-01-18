package x590.jdecompiler.operation.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public final class DLoadOperation extends LoadOperation {
	
	public DLoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.DOUBLE, context, index);
	}
}
