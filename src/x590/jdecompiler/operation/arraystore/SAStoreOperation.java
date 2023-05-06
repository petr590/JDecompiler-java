package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class SAStoreOperation extends ArrayStoreOperation {
	
	public SAStoreOperation(DecompilationContext context) {
		super(ArrayType.SHORT_ARRAY, context);
	}
}
