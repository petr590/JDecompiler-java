package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public final class AALoadOperation extends ArrayLoadOperation {
	
	public AALoadOperation(DecompilationContext context) {
		super(ArrayType.ANY_OBJECT_ARRAY, context);
	}
}
