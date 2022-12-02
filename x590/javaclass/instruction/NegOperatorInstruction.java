package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.NegOperatorOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;

public class NegOperatorInstruction extends OperatorInstruction {
	
	public NegOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new NegOperatorOperation(type, context);
	}
}