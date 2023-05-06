package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class FALoadOperation extends ArrayLoadOperation {
	
	public FALoadOperation(DecompilationContext context) {
		super(ArrayType.FLOAT_ARRAY, context);
	}
}
