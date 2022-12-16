package x590.javaclass.operation.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class IStoreOperation extends StoreOperation {

	public IStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.BYTE_SHORT_CHAR_INT_BOOLEAN, context, index);
	}
}