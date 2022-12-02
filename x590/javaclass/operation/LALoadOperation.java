package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class LALoadOperation extends ArrayLoadOperation {

	public LALoadOperation(DecompilationContext context) {
		super(ArrayType.LONG_ARRAY, context);
	}
}