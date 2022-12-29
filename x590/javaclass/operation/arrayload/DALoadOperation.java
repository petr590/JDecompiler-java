package x590.javaclass.operation.arrayload;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class DALoadOperation extends ArrayLoadOperation {
	
	public DALoadOperation(DecompilationContext context) {
		super(ArrayType.DOUBLE_ARRAY, context);
	}
}
