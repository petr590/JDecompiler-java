package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class AALoadOperation extends ArrayLoadOperation {

	public AALoadOperation(DecompilationContext context) {
		super(ArrayType.ANY_OBJECT_ARRAY, context);
	}
}