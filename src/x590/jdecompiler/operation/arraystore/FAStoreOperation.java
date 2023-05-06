package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class FAStoreOperation extends ArrayStoreOperation {
	
	public FAStoreOperation(DecompilationContext context) {
		super(ArrayType.FLOAT_ARRAY, context);
	}
}
