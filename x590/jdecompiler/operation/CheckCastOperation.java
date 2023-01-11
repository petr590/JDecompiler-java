package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.Types;

public class CheckCastOperation extends CastOperation {
	
	public CheckCastOperation(DecompilationContext context, int index) {
		super(Types.ANY_TYPE, context.pool.getClassConstant(index).toReferenceType(), false, context);
	}
}
