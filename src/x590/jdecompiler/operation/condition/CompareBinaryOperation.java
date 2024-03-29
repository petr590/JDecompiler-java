package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.cmp.CmpOperation;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.Type;

public final class CompareBinaryOperation extends CompareOperation {
	
	private final Operation operand1, operand2;
	
	// Обратный порядок операндов, так как со стека они снимаются именно в обратном порядке
	private CompareBinaryOperation(Operation operand2, Operation operand1, CompareType compareType, Type requiredType) {
		super(compareType);
		
		operand1 = operand1.useAsNarrowest(requiredType);
		operand2 = operand2.useAsNarrowest(requiredType);
		
		this.operand1 = operand1;
		this.operand2 = operand2;
		
		if(!compareType.isEqualsCompareType()) {
			operand1.allowImplicitCast();
			operand2.allowImplicitCast();
		}
		
		Type generalType = operand1.getReturnType().castToGeneral(operand2.getReturnType(),
				compareType.isEqualsCompareType() ? GeneralCastingKind.EQUALS_COMPARASION : GeneralCastingKind.COMPARASION);
		
		operand1 = operand1.useAsNarrowest(generalType);
		operand2 = operand2.useAsNarrowest(generalType);
		
		if(generalType.isLongOrFloatOrDouble()) {
			boolean allowedImplCast1 = operand1.implicitCastAllowed(),
					allowedImplCast2 = operand2.implicitCastAllowed();
			     
			if(allowedImplCast1 ^ allowedImplCast2) { // Разрешить приведение, когда только один из типов может быть приведен неявно
				(allowedImplCast1 ? operand1 : operand2).allowImplicitCast();
			}
		}
	}
	
	public CompareBinaryOperation(CmpOperation cmpOperation, CompareType compareType) {
		this(cmpOperation.operand2, cmpOperation.operand1, compareType, compareType.getRequiredType());
	}
	
	public CompareBinaryOperation(DecompilationContext context, CompareType compareType, Type requiredType) {
		this(context.pop(), context.pop(), compareType, compareType.getRequiredType().castToNarrowest(requiredType));
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand1, context, Associativity.LEFT).printsp()
			.print(getCompareType().getOperator(inverted)).printsp()
			.printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof CompareBinaryOperation operation &&
				operand1.equals(operation.operand1) && operand2.equals(operation.operand2);
	}
}
