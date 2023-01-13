package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;

public abstract class UnaryOperatorOperation extends OperatorOperation {
	
	protected final Operation operand;
	
	public UnaryOperatorOperation(Type type, DecompilationContext context) {
		super(type);
		this.operand = context.popAsNarrowest(type);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(getOperator()).printPrioritied(this, operand, context, Associativity.RIGHT);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return operand.requiresLocalContext();
	}
}
