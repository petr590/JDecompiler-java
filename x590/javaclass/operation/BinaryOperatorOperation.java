package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public abstract class BinaryOperatorOperation extends OperatorOperation {
	
	protected final Operation operand1, operand2;
	
	public BinaryOperatorOperation(Type type, DecompilationContext context) {
		super(type);
		this.operand2 = context.stack.popAsNarrowest(type);
		this.operand1 = context.stack.popAsNarrowest(type);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand1, context, Associativity.LEFT).printsp().print(getOperator())
				.printsp().printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return operand1.requiresLocalContext() || operand2.requiresLocalContext();
	}
}