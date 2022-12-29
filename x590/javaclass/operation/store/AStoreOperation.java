package x590.javaclass.operation.store;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Types;

public class AStoreOperation extends StoreOperation {
	
	public AStoreOperation(DecompilationContext context, int index) {
		super(Types.ANY_OBJECT_TYPE, context, index);
	}
	
	@Override
	public boolean canIncrement() {
		return false;
	}
}
