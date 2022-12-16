package x590.javaclass.operation.returning;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class DReturnOperation extends ReturnOperation {
	
	public DReturnOperation(DecompilationContext context) {
		super(PrimitiveType.DOUBLE, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "dreturn";
	}
}