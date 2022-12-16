package x590.javaclass.operation.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class FStoreOperation extends StoreOperation {

	public FStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.FLOAT, context, index);
	}
}