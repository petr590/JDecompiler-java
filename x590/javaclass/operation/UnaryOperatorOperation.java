package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public abstract class UnaryOperatorOperation extends OperatorOperation {
	
	protected final Operation operand;
	
	public UnaryOperatorOperation(Type type, DecompilationContext context) {
		super(type);
		this.operand = context.stack.popAsNarrowest(type);
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