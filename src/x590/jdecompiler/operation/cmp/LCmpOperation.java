package x590.jdecompiler.operation.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public final class LCmpOperation extends CmpOperation {
	
	public LCmpOperation(DecompilationContext context) {
		super(PrimitiveType.LONG, context);
	}
}
