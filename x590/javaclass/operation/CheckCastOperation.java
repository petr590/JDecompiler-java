package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Types;

public class CheckCastOperation extends CastOperation {
	
	public CheckCastOperation(DecompilationContext context, int index) {
		super(Types.ANY_TYPE, context.pool.getClassConstant(index).toReferenceType(), false, context);
	}
}