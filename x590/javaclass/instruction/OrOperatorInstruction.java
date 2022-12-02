package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.OrOperatorOperation;
import x590.javaclass.type.Type;
import x590.javaclass.operation.Operation;

public class OrOperatorInstruction extends OperatorInstruction {
	
	public OrOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new OrOperatorOperation(type, context);
	}
}