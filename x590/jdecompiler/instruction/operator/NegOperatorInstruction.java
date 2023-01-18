package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.operator.NegOperatorOperation;
import x590.jdecompiler.type.Type;

public class NegOperatorInstruction extends OperatorInstruction {
	
	public NegOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new NegOperatorOperation(type, context);
	}
}
