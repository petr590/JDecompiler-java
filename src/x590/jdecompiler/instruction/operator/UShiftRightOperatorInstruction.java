package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.operator.UShiftRightOperatorOperation;
import x590.jdecompiler.type.Type;

public class UShiftRightOperatorInstruction extends OperatorInstruction {
	
	public UShiftRightOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new UShiftRightOperatorOperation(type, context);
	}
}
