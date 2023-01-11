package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.operator.OrOperatorOperation;

public class OrOperatorInstruction extends OperatorInstruction {
	
	public OrOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new OrOperatorOperation(type, context);
	}
}
