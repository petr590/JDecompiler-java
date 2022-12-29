package x590.javaclass.operation.arraystore;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class LAStoreOperation extends ArrayStoreOperation {
	
	public LAStoreOperation(DecompilationContext context) {
		super(ArrayType.LONG_ARRAY, context);
	}
}
