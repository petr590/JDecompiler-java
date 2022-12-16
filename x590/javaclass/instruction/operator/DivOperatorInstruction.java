package x590.javaclass.instruction.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.operator.DivOperatorOperation;
import x590.javaclass.type.Type;

public class DivOperatorInstruction extends OperatorInstruction {
	
	public DivOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DivOperatorOperation(type, context);
	}
}