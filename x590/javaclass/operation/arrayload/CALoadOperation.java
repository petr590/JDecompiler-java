package x590.javaclass.operation.arrayload;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class CALoadOperation extends ArrayLoadOperation {

	public CALoadOperation(DecompilationContext context) {
		super(ArrayType.CHAR_ARRAY, context);
	}
}