package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.operator.DivOperatorOperation;
import x590.jdecompiler.type.Type;

public class DivOperatorInstruction extends OperatorInstruction {
	
	public DivOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DivOperatorOperation(type, context);
	}
}
