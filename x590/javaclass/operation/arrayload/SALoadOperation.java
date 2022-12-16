package x590.javaclass.operation.arrayload;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class SALoadOperation extends ArrayLoadOperation {

	public SALoadOperation(DecompilationContext context) {
		super(ArrayType.SHORT_ARRAY, context);
	}
}