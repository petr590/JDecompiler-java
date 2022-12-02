package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class IReturnOperation extends ReturnOperation {
	
	public IReturnOperation(DecompilationContext context) {
		super(PrimitiveType.ANY_INT_OR_BOOLEAN, context);
	}
}