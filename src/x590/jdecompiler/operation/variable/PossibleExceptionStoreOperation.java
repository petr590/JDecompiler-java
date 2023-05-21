package x590.jdecompiler.operation.variable;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.load.ExceptionLoadOperation;
import x590.jdecompiler.scope.CatchScope;
import x590.jdecompiler.variable.Variable;

import x590.util.annotation.Nullable;

public interface PossibleExceptionStoreOperation extends Operation {
	
	public default boolean removeIfExceptionLoadOperation(DecompilationContext context, Operation value) {
		if( value instanceof ExceptionLoadOperation &&
			context.currentScope() instanceof CatchScope catchScope &&
			context.currentIndex() == catchScope.startIndex() + 1) {
			
			this.remove();
			return true;
		}
		
		return false;
	}
	
	public @Nullable Variable getStoringVariable();
}
