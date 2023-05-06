package x590.jdecompiler.operation.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class LStoreOperation extends StoreOperation {
	
	public LStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.LONG, context, index);
	}
}
