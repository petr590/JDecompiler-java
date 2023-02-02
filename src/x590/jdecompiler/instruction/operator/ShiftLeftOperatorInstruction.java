package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.operator.ShiftLeftOperatorOperation;
import x590.jdecompiler.type.Type;

public class ShiftLeftOperatorInstruction extends OperatorInstruction {
	
	public ShiftLeftOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ShiftLeftOperatorOperation(type, context);
	}
}
