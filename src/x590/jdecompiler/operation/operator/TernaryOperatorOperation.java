package x590.jdecompiler.operation.operator;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;

public final class TernaryOperatorOperation extends ReturnableOperation {
	
	private final ConditionOperation condition;
	private final Operation operand1, operand2;
	
	public TernaryOperatorOperation(ConditionOperation condition, Operation operand1, Operation operand2) {
		super(PrimitiveType.VOID);
		this.condition = condition;
		this.operand1 = operand1;
		this.operand2 = operand2;
		
		returnType = operand1.getReturnTypeAsGeneralNarrowest(operand2, GeneralCastingKind.TERNARY_OPERATOR);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(isBooleanCondition()) {
			out.print(((IConstOperation)operand1).getValue() != 0 ? condition : condition.invert(), context);
		} else {
			out.print(condition, context).print(" ? ")
				.printPrioritied(this, operand1, context, Associativity.LEFT).print(" : ")
				.printPrioritied(this, operand2, context, Associativity.RIGHT);
		}
	}
	
	private boolean isBooleanCondition() {
		return returnType.isSubtypeOf(PrimitiveType.BOOLEAN) &&
				operand1 instanceof IConstOperation iconst1 &&
				operand2 instanceof IConstOperation iconst2 &&
				(iconst1.getValue() == 1 && iconst2.getValue() == 0 ||
				 iconst1.getValue() == 0 && iconst2.getValue() == 1);
	}
	
	@Override
	public int getPriority() {
		return isBooleanCondition() ? condition.getPriority() : Priority.TERNARY_OPERATOR;
	}
	
	@Override
	public void onCastReturnType(Type newType, CastingKind kind) {
		super.onCastReturnType(newType, kind);
		operand1.castReturnTypeTo(returnType, kind);
		operand2.castReturnTypeTo(returnType, kind);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof TernaryOperatorOperation operation &&
				super.equals(operation) && condition.equals(operation.condition) &&
				operand1.equals(operation.operand1) && operand2.equals(operation.operand2);
	}
}
