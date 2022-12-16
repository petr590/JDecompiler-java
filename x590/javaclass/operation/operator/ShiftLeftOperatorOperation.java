package x590.javaclass.operation.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Type;

public class ShiftLeftOperatorOperation extends ShiftOperatorOperation {
	
	public ShiftLeftOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	public String getOperator() {
		return "<<";
	}
}