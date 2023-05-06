package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.reference.ArrayType;

public final class CALoadOperation extends ArrayLoadOperation {
	
	public CALoadOperation(DecompilationContext context) {
		super(ArrayType.CHAR_ARRAY, context);
	}
}
