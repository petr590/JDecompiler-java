package x590.javaclass.operation.returning;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class FReturnOperation extends ReturnOperation {
	
	public FReturnOperation(DecompilationContext context) {
		super(PrimitiveType.FLOAT, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "freturn";
	}
}