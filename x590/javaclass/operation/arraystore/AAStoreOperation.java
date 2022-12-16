package x590.javaclass.operation.arraystore;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class AAStoreOperation extends ArrayStoreOperation {
	
	public AAStoreOperation(DecompilationContext context) {
		super(ArrayType.ANY_OBJECT_ARRAY, context);
	}
}