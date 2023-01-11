package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

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
