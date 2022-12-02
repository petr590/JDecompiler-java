package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class SAStoreOperation extends ArrayStoreOperation {

	public SAStoreOperation(DecompilationContext context) {
		super(ArrayType.SHORT_ARRAY, context);
	}
}