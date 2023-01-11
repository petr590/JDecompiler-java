package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public class LALoadOperation extends ArrayLoadOperation {
	
	public LALoadOperation(DecompilationContext context) {
		super(ArrayType.LONG_ARRAY, context);
	}
}
