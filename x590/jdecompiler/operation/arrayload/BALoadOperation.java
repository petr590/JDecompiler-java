package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public class BALoadOperation extends ArrayLoadOperation {
	
	public BALoadOperation(DecompilationContext context) {
		super(ArrayType.BYTE_OR_BOOLEAN_ARRAY, context);
	}
}
