package x590.jdecompiler.operation.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public class DStoreOperation extends StoreOperation {
	
	public DStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.DOUBLE, context, index);
	}
}
