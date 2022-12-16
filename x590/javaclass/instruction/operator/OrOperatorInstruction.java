package x590.javaclass.instruction.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Type;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.operator.OrOperatorOperation;

public class OrOperatorInstruction extends OperatorInstruction {
	
	public OrOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new OrOperatorOperation(type, context);
	}
}