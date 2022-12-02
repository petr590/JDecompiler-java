package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Types;

public class AReturnOperation extends ReturnOperation {
	
	public AReturnOperation(DecompilationContext context) {
		super(Types.ANY_OBJECT_TYPE, context);
	}
}