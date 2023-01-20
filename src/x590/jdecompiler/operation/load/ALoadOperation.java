package x590.jdecompiler.operation.load;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.Types;

public final class ALoadOperation extends LoadOperation {
	
	public ALoadOperation(DecompilationContext context, int index) {
		super(Types.ANY_OBJECT_TYPE, context, index);
	}
}
