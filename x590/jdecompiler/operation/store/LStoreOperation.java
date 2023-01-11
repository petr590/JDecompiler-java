package x590.jdecompiler.operation.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public class LStoreOperation extends StoreOperation {
	
	public LStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.LONG, context, index);
	}
}
