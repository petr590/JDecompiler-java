package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class DStoreOperation extends StoreOperation {

	public DStoreOperation(DecompilationContext context, int index) {
		super(PrimitiveType.DOUBLE, context, index);
	}
}