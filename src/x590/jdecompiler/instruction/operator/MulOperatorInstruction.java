package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.operator.MulOperatorOperation;
import x590.jdecompiler.type.Type;

public class MulOperatorInstruction extends OperatorInstruction {
	
	public MulOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new MulOperatorOperation(type, context);
	}
}
