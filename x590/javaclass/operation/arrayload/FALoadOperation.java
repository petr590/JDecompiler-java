package x590.javaclass.operation.arrayload;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class FALoadOperation extends ArrayLoadOperation {
	
	public FALoadOperation(DecompilationContext context) {
		super(ArrayType.FLOAT_ARRAY, context);
	}
}
