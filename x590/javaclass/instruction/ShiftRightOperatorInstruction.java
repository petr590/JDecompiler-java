package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.ShiftRightOperatorOperation;
import x590.javaclass.type.Type;

public class ShiftRightOperatorInstruction extends OperatorInstruction {
	
	public ShiftRightOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ShiftRightOperatorOperation(type, context);
	}
}