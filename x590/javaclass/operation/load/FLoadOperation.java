package x590.javaclass.operation.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class FLoadOperation extends LoadOperation {
	
	public FLoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.FLOAT, context, index);
	}
}
