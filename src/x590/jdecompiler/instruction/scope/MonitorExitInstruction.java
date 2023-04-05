package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.load.ALoadOperation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.scope.SynchronizedScope;
import x590.jdecompiler.type.Types;
import x590.jdecompiler.variable.EmptyableVariable;
import x590.util.annotation.Nullable;

public class MonitorExitInstruction extends ScopeInstruction {
	
	@Override
	protected @Nullable Scope toScope(DecompilationContext context) {
		Operation value = context.popAsNarrowest(Types.ANY_OBJECT_TYPE);
		
		if(value instanceof ALoadOperation aloadOperation) {
			aloadOperation.remove();
			EmptyableVariable variable = aloadOperation.getVariable();
			
			for(Scope scope : context.getCurrentScopes()) {
				if(scope instanceof SynchronizedScope synchronizedScope && synchronizedScope.getVariable() == variable) {
					synchronizedScope.setEndIndex(context.currentIndex());
				}
			}
			
		} else {
			context.warning("Cannot find variable for monitorexit instruction, maybe code is broken");
		}
		
		return null;
	}
}
