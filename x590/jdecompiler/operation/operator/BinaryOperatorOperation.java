package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;

public abstract class BinaryOperatorOperation extends OperatorOperation {
	
	protected final Operation operand1;
	protected final Operation operand2;
	
	public BinaryOperatorOperation(Type type, DecompilationContext context) {
		super(type);
		this.operand2 = context.stack.popAsNarrowest(type);
		this.operand1 = context.stack.popAsNarrowest(type);
		
		returnType = operand2.getReturnTypeAsGeneralNarrowest(operand1);
	}
	
	public BinaryOperatorOperation(Type type1, Type type2, DecompilationContext context) {
		super(type1);
		this.operand2 = context.stack.popAsNarrowest(type2);
		this.operand1 = context.stack.popAsNarrowest(type1);
	}
	
	public Operation operand1() {
		return operand1;
	}
	
	public Operation operand2() {
		return operand2;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand1, context, Associativity.LEFT).printsp().print(getOperator())
				.printsp().printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		super.onCastReturnType(newType);
		operand1.castReturnTypeToNarrowest(newType);
		operand2.castReturnTypeToNarrowest(newType);
	}
	
	protected void onSuperCastReturnType(Type newType) {
		super.onCastReturnType(newType);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return operand1.requiresLocalContext() || operand2.requiresLocalContext();
	}
}
