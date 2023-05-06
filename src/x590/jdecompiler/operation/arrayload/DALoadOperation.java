package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class DALoadOperation extends ArrayLoadOperation {
	
	public DALoadOperation(DecompilationContext context) {
		super(ArrayType.DOUBLE_ARRAY, context);
	}
}
