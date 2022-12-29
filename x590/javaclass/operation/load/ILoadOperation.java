package x590.javaclass.operation.load;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class ILoadOperation extends LoadOperation {
	
	public ILoadOperation(DecompilationContext context, int index) {
		super(PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN, context, index);
	}
}
