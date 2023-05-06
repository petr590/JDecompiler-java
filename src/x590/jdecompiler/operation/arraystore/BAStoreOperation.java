package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class BAStoreOperation extends ArrayStoreOperation {
	
	public BAStoreOperation(DecompilationContext context) {
		super(ArrayType.BYTE_OR_BOOLEAN_ARRAY, context);
	}
}
