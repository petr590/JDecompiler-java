package x590.javaclass.instruction.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.operator.ShiftLeftOperatorOperation;
import x590.javaclass.type.Type;

public class ShiftLeftOperatorInstruction extends OperatorInstruction {
	
	public ShiftLeftOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ShiftLeftOperatorOperation(type, context);
	}
}