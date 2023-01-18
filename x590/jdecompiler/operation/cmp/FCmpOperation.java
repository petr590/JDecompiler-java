package x590.jdecompiler.operation.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public final class FCmpOperation extends CmpOperation {
	
	public FCmpOperation(DecompilationContext context) {
		super(PrimitiveType.FLOAT, context);
	}
}
