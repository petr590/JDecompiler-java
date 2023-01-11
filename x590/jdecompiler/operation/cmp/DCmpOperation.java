package x590.jdecompiler.operation.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public class DCmpOperation extends CmpOperation {
	
	public DCmpOperation(DecompilationContext context) {
		super(PrimitiveType.DOUBLE, context);
	}
}
