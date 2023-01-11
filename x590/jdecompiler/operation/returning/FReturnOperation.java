package x590.jdecompiler.operation.returning;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.PrimitiveType;

public class FReturnOperation extends ReturnOperation {
	
	public FReturnOperation(DecompilationContext context) {
		super(PrimitiveType.FLOAT, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "freturn";
	}
}
