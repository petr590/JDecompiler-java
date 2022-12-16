package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.load.ALoadOperation;
import x590.javaclass.scope.Scope;
import x590.javaclass.scope.SynchronizedScope;
import x590.javaclass.type.Types;
import x590.javaclass.variable.EmptyableVariable;

public class MonitorExitInstruction extends ScopeInstruction {
	
	@Override
	public Scope toScope(DecompilationContext context) {
		Operation value = context.stack.popAsNarrowest(Types.ANY_OBJECT_TYPE);
		
		if(value instanceof ALoadOperation aloadOperation) {
			aloadOperation.remove();
			EmptyableVariable variable = aloadOperation.getVariable();
			
			for(Scope scope : context.getScopes()) {
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