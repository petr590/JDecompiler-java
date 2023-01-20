package x590.jdecompiler.operation.array;

import x590.jdecompiler.context.DecompilationContext;

public final class MultiANewArrayOperation extends NewArrayOperation {
	
	public MultiANewArrayOperation(DecompilationContext context, int index, int dimensions) {
		super(context, index, dimensions);
	}
}
