package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.operation.operator.RemOperatorOperation;

public class RemOperatorInstruction extends OperatorInstruction {
	
	public RemOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new RemOperatorOperation(type, context);
	}
}
