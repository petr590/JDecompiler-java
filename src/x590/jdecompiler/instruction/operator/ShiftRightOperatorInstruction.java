package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.operator.ShiftRightOperatorOperation;
import x590.jdecompiler.type.Type;

public class ShiftRightOperatorInstruction extends OperatorInstruction {
	
	public ShiftRightOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ShiftRightOperatorOperation(type, context);
	}
}
