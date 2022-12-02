package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class DCmpOperation extends CmpOperation {
	
	public DCmpOperation(DecompilationContext context) {
		super(PrimitiveType.DOUBLE, context);
	}
}