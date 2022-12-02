package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class BALoadOperation extends ArrayLoadOperation {

	public BALoadOperation(DecompilationContext context) {
		super(ArrayType.BYTE_OR_BOOLEAN_ARRAY, context);
	}
}