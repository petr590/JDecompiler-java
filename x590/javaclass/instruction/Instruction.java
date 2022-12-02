package x590.javaclass.instruction;

import javax.annotation.Nullable;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.scope.Scope;

public abstract class Instruction {
	
	public Instruction() {}
	
	@Nullable
	public abstract Operation toOperation(DecompilationContext context);
	
	@Nullable
	public Scope toScope(DecompilationContext context) {
		return null;
	}
}