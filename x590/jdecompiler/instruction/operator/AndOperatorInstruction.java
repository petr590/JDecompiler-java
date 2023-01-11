package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.operator.AndOperatorOperation;
import x590.jdecompiler.type.Type;

public class AndOperatorInstruction extends OperatorInstruction {
	
	public AndOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new AndOperatorOperation(type, context);
	}
}
