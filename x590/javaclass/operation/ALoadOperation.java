package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Types;

public class ALoadOperation extends LoadOperation {

	public ALoadOperation(DecompilationContext context, int index) {
		super(Types.ANY_OBJECT_TYPE, context, index);
	}
}