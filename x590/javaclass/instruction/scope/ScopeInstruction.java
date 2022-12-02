package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.scope.Scope;

public abstract class ScopeInstruction extends Instruction {
	
	@Override
	public final Operation toOperation(DecompilationContext context) {
		return toScope(context);
	}
	
	@Override
	public abstract Scope toScope(DecompilationContext context);
}