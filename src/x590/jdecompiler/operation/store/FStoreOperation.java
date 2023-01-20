package x590.jdecompiler.operation.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public final class FStoreOperation extends StoreOperation {
	
	public FStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.FLOAT, context, index);
	}
}
