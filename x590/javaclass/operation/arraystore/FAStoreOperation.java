package x590.javaclass.operation.arraystore;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class FAStoreOperation extends ArrayStoreOperation {

	public FAStoreOperation(DecompilationContext context) {
		super(ArrayType.FLOAT_ARRAY, context);
	}
}