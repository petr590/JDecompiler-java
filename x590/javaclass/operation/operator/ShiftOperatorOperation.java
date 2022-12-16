package x590.javaclass.operation.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Priority;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public abstract class ShiftOperatorOperation extends BinaryOperatorOperation {
	
	public ShiftOperatorOperation(Type type, DecompilationContext context) {
		super(type, PrimitiveType.INT, context);
	}
	
	@Override
	public int getPriority() {
		return Priority.SHIFT;
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		onSuperCastReturnType(newType);
		operand1.castReturnTypeToNarrowest(newType);
	}
}