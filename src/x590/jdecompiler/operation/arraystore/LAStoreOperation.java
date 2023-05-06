package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class LAStoreOperation extends ArrayStoreOperation {
	
	public LAStoreOperation(DecompilationContext context) {
		super(ArrayType.LONG_ARRAY, context);
	}
}
