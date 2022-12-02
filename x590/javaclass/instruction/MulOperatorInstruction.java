package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.MulOperatorOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;

public class MulOperatorInstruction extends OperatorInstruction {
	
	public MulOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new MulOperatorOperation(type, context);
	}
}