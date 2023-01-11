package x590.jdecompiler.operation.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public class FLoadOperation extends LoadOperation {
	
	public FLoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.FLOAT, context, index);
	}
}
