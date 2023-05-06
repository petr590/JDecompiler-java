package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class CAStoreOperation extends ArrayStoreOperation {
	
	public CAStoreOperation(DecompilationContext context) {
		super(ArrayType.CHAR_ARRAY, context);
	}
}
