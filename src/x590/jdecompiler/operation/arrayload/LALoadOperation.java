package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class LALoadOperation extends ArrayLoadOperation {
	
	public LALoadOperation(DecompilationContext context) {
		super(ArrayType.LONG_ARRAY, context);
	}
}
