package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public final class SALoadOperation extends ArrayLoadOperation {
	
	public SALoadOperation(DecompilationContext context) {
		super(ArrayType.SHORT_ARRAY, context);
	}
}
