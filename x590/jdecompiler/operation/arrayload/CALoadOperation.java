package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public class CALoadOperation extends ArrayLoadOperation {
	
	public CALoadOperation(DecompilationContext context) {
		super(ArrayType.CHAR_ARRAY, context);
	}
}
