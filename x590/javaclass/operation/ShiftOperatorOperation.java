package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Type;

public abstract class ShiftOperatorOperation extends BinaryOperatorOperation {
	
	public ShiftOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	public int getPriority() {
		return Priority.SHIFT;
	}
}