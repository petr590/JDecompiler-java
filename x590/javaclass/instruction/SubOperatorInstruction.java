package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.SubOperatorOperation;
import x590.javaclass.type.Type;
import x590.javaclass.operation.Operation;

public class SubOperatorInstruction extends OperatorInstruction {
	
	public SubOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new SubOperatorOperation(type, context);
	}
}