package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.AndOperatorOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;

public class AndOperatorInstruction extends OperatorInstruction {
	
	public AndOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AndOperatorOperation(type, context);
	}
}