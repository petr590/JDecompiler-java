package x590.javaclass.operation.arraystore;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class DAStoreOperation extends ArrayStoreOperation {

	public DAStoreOperation(DecompilationContext context) {
		super(ArrayType.DOUBLE_ARRAY, context);
	}
}