package x590.jdecompiler.operation.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class DCmpOperation extends CmpOperation {
	
	public DCmpOperation(DecompilationContext context) {
		super(PrimitiveType.DOUBLE, context);
	}
}
