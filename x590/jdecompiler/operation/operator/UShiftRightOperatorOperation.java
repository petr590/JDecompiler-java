package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.Type;

public final class UShiftRightOperatorOperation extends ShiftOperatorOperation {
	
	public UShiftRightOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	public String getOperator() {
		return ">>>";
	}
}
