package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.Type;

public class AddOperatorOperation extends BinaryOperatorOperation {
	
	public AddOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	public String getOperator() {
		return "+";
	}
	
	@Override
	public int getPriority() {
		return Priority.PLUS;
	}
}