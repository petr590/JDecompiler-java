package x590.jdecompiler.operation.array;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public final class ANewArrayOperation extends NewArrayOperation {
	
	public ANewArrayOperation(DecompilationContext context, int index) {
		super(context, ArrayType.forType(context.pool.getClassConstant(index).toReferenceType()));
	}
}
