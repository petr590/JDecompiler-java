package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public final class DAStoreOperation extends ArrayStoreOperation {
	
	public DAStoreOperation(DecompilationContext context) {
		super(ArrayType.DOUBLE_ARRAY, context);
	}
}
