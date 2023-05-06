package x590.jdecompiler.operation.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class LReturnOperation extends ReturnOperation {
	
	public LReturnOperation(DecompilationContext context) {
		super(PrimitiveType.LONG, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "lreturn";
	}
}
