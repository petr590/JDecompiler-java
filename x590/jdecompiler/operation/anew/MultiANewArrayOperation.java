package x590.jdecompiler.operation.anew;

import x590.jdecompiler.context.DecompilationContext;

public class MultiANewArrayOperation extends NewArrayOperation {
	
	public MultiANewArrayOperation(DecompilationContext context, int index, int dimensions) {
		super(context, index, dimensions);
	}
}
