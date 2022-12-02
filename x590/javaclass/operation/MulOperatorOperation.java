package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Type;

public class MulOperatorOperation extends BinaryOperatorOperation {
	
	public MulOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	protected String getOperator() {
		return "*";
	}
	
	@Override
	public int getPriority() {
		return Priority.MULTIPLE;
	}
}