package x590.javaclass.operation.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class DLoadOperation extends LoadOperation {

	public DLoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.DOUBLE, context, index);
	}
}