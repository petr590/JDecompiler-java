package x590.jdecompiler.operation.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class DReturnOperation extends ReturnOperation {
	
	public DReturnOperation(DecompilationContext context) {
		super(PrimitiveType.DOUBLE, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "dreturn";
	}
}
