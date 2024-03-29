package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.Type;

public abstract class BinaryOperatorOperation extends OperatorOperation {
	
	private Operation operand1, operand2;
	
	/** Для того, чтобы можно было преобразовать операнд перед его присвоением */
	protected Operation processOperand1(Operation operand1) {
		return operand1;
	}
	
	/** Для того, чтобы можно было преобразовать операнд перед его присвоением */
	protected Operation processOperand2(Operation operand2) {
		return operand2;
	}
	
	public BinaryOperatorOperation(Type type, DecompilationContext context) {
		super(type);
		var operand2 = this.operand2 = processOperand2(context.popAsNarrowest(type));
		var operand1 = this.operand1 = processOperand1(context.popAsNarrowest(type));
		
		returnType = operand1.getReturnTypeAsGeneralNarrowest(operand2, GeneralCastingKind.BINARY_OPERATOR);
		
		Type implicitGeneralType = operand1.getImplicitType().implicitCastToGeneralNoexcept(operand2.getImplicitType(), GeneralCastingKind.BINARY_OPERATOR);
		
		if(implicitGeneralType != null && implicitGeneralType.equals(returnType)) {
			operand2.allowImplicitCast();
			operand1.allowImplicitCast();
		}
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
	
	protected void operand1(Operation operand1) {
		this.operand1 = operand1;
	}
	
	protected void operand2(Operation operand2) {
		this.operand2 = operand2;
	}
	
	
	@Override
	protected Type getDeducedType(Type returnType) {
		return operand1.getReturnTypeAsGeneralNarrowest(operand2, GeneralCastingKind.BINARY_OPERATOR);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand1, context, Associativity.LEFT)
			.printsp().print(getOperator()).printsp()
			.printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
	
	@Override
	public void onCastReturnType(Type newType, CastingKind kind) {
		super.onCastReturnType(newType, kind);
		operand1 = operand1.useAs(newType, kind);
		operand2 = operand2.useAs(newType, kind);
	}
	
	protected void superOnCastReturnType(Type newType, CastingKind kind) {
		super.onCastReturnType(newType, kind);
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
