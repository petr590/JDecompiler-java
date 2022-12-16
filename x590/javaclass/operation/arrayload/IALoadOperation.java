package x590.javaclass.operation.arrayload;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class IALoadOperation extends ArrayLoadOperation {

	public IALoadOperation(DecompilationContext context) {
		super(ArrayType.INT_ARRAY, context);
	}
}