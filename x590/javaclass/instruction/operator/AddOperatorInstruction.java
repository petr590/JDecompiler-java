package x590.javaclass.instruction.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.operator.AddOperatorOperation;
import x590.javaclass.type.Type;

public class AddOperatorInstruction extends OperatorInstruction {
	
	public AddOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AddOperatorOperation(type, context);
	}
}