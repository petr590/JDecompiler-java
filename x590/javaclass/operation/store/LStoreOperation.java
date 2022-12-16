package x590.javaclass.operation.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class LStoreOperation extends StoreOperation {

	public LStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.LONG, context, index);
	}
}