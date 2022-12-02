package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class ILoadOperation extends LoadOperation {

	public ILoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.ANY_INT_OR_BOOLEAN, context, index);
	}
}