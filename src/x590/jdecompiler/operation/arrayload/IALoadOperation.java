package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class IALoadOperation extends ArrayLoadOperation {
	
	public IALoadOperation(DecompilationContext context) {
		super(ArrayType.INT_ARRAY, context);
	}
}
