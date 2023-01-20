package x590.jdecompiler.operation.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.Types;

public final class AReturnOperation extends ReturnOperation {
	
	public AReturnOperation(DecompilationContext context) {
		super(Types.ANY_OBJECT_TYPE, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "areturn";
	}
}
