package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.util.Logger;

public abstract class ShiftOperatorOperation extends BinaryOperatorOperation {
	
	public ShiftOperatorOperation(Type type, DecompilationContext context) {
		super(type, PrimitiveType.INT, context);
	}
	
	@Override
	protected Operation processOperand2(Operation operand2) {
		Logger.debug(operand2 instanceof CastOperation);
		
		if(operand2 instanceof CastOperation cast && cast.getRequiredType() == PrimitiveType.LONG && cast.getCastedType() == PrimitiveType.INT) {
			return cast.getOperand();
		}
		
		return operand2;
	}
	
	@Override
	public int getPriority() {
		return Priority.SHIFT;
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		onSuperCastReturnType(newType);
		operand1().castReturnTypeToNarrowest(newType);
	}
}