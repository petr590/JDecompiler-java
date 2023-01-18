package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.Type;

public abstract class BinaryOperatorOperation extends OperatorOperation {
	
	private final Operation operand1;
	private final Operation operand2;
	
	protected Operation processOperand1(Operation operand1) {
		return operand1;
	}
	
	protected Operation processOperand2(Operation operand2) {
		return operand2;
	}
	
	public BinaryOperatorOperation(Type type, DecompilationContext context) {
		super(type);
		this.operand2 = processOperand2(context.popAsNarrowest(type));
		this.operand1 = (context.popAsNarrowest(type));
		
		returnType = operand2.getReturnTypeAsGeneralNarrowest(operand1);
	}
	
	public BinaryOperatorOperation(Type type1, Type type2, DecompilationContext context) {
		super(type1);
		this.operand2 = processOperand2(context.popAsNarrowest(type2));
		this.operand1 = processOperand1(context.popAsNarrowest(type1));
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
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof BinaryOperatorOperation operation &&
				super.equals(operation) && operand1.equals(operation.operand1) && operand2.equals(operation.operand2);
	}
}
