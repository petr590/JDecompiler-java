package x590.javaclass.operation.arraystore;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class IAStoreOperation extends ArrayStoreOperation {

	public IAStoreOperation(DecompilationContext context) {
		super(ArrayType.INT_ARRAY, context);
	}
}