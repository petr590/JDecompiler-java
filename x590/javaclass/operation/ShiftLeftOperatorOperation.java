package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Type;

public class ShiftLeftOperatorOperation extends ShiftOperatorOperation {
	
	public ShiftLeftOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	protected String getOperator() {
		return "<<";
	}
}