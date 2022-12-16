package x590.javaclass.instruction.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Type;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.operator.RemOperatorOperation;

public class RemOperatorInstruction extends OperatorInstruction {
	
	public RemOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new RemOperatorOperation(type, context);
	}
}