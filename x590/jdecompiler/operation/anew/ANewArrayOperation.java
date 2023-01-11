package x590.jdecompiler.operation.anew;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.ArrayType;

public class ANewArrayOperation extends NewArrayOperation {
	
	public ANewArrayOperation(DecompilationContext context, int index) {
		super(context, ArrayType.forType(context.pool.getClassConstant(index).toReferenceType()));
	}
}
