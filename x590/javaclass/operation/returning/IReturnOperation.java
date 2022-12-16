package x590.javaclass.operation.returning;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.PrimitiveType;

public class IReturnOperation extends ReturnOperation {
	
	public IReturnOperation(DecompilationContext context) {
		super(PrimitiveType.BYTE_SHORT_CHAR_INT_BOOLEAN, context);
	}
	
	@Override
	protected String getInstructionName() {
		return "ireturn";
	}
}