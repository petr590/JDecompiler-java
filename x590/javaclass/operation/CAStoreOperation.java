package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class CAStoreOperation extends ArrayStoreOperation {

	public CAStoreOperation(DecompilationContext context) {
		super(ArrayType.CHAR_ARRAY, context);
	}
}