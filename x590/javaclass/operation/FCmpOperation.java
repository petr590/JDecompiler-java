package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class FCmpOperation extends CmpOperation {
	
	public FCmpOperation(DecompilationContext context) {
		super(PrimitiveType.FLOAT, context);
	}
}