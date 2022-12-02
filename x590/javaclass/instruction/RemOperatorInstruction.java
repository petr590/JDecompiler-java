package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.RemOperatorOperation;
import x590.javaclass.type.Type;
import x590.javaclass.operation.Operation;

public class RemOperatorInstruction extends OperatorInstruction {
	
	public RemOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new RemOperatorOperation(type, context);
	}
}