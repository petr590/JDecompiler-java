package x590.javaclass.operation.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class LLoadOperation extends LoadOperation {

	public LLoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.LONG, context, index);
	}
}