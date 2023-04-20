package x590.jdecompiler.operation.store;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.Types;
import x590.jdecompiler.variable.Variable;

public final class AStoreOperation extends StoreOperation {
	
	public AStoreOperation(DecompilationContext context, int index) {
		super(Types.ANY_OBJECT_TYPE, context, index);
	}
	
	@Override
	public Variable getStoringVariable() {
		return variable;
	}
}
