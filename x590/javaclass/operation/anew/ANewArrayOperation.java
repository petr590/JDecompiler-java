package x590.javaclass.operation.anew;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.ArrayType;

public class ANewArrayOperation extends NewArrayOperation {
	
	public ANewArrayOperation(DecompilationContext context, int index) {
		super(context, new ArrayType(context.pool.getClassConstant(index).toArrayType()));
	}
}