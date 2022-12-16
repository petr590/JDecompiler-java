package x590.javaclass.operation.cmp;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class LCmpOperation extends CmpOperation {
	
	public LCmpOperation(DecompilationContext context) {
		super(PrimitiveType.LONG, context);
	}
}