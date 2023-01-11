package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public class IAStoreOperation extends ArrayStoreOperation {
	
	public IAStoreOperation(DecompilationContext context) {
		super(ArrayType.INT_ARRAY, context);
	}
}
