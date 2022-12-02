package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.UShiftRightOperatorOperation;
import x590.javaclass.type.Type;

public class UShiftRightOperatorInstruction extends OperatorInstruction {
	
	public UShiftRightOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new UShiftRightOperatorOperation(type, context);
	}
}