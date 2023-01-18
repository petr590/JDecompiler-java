package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.cmp.CmpOperation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public final class CompareBinaryOperation extends CompareOperation {
	
	private final Operation operand1, operand2;
	
	// Обратный порядок операндов, так как со стека они снимаются именно в обратном порядке
	private CompareBinaryOperation(Operation operand2, Operation operand1, CompareType compareType, Type requiredType) {
		super(compareType);
		this.operand1 = operand1;
		this.operand2 = operand2;
		
		operand1.castReturnTypeToNarrowest(requiredType);
		operand2.castReturnTypeToNarrowest(requiredType);
		
		if(!compareType.isEqualsCompareType) {
			operand1.allowImplicitCast();
			operand2.allowImplicitCast();
		}
		
		Type generalType = operand1.getReturnType().castToGeneral(operand2.getReturnType());
		
		operand1.castReturnTypeToNarrowest(generalType);
		operand2.castReturnTypeToNarrowest(generalType);
		
		if(generalType.isPrimitive() && (generalType == PrimitiveType.LONG || generalType == PrimitiveType.FLOAT || generalType == PrimitiveType.DOUBLE)) {
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
		out.printPrioritied(this, operand1, context, Associativity.LEFT).printsp().print(compareType.getOperator(inverted)).printsp().printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof CompareBinaryOperation operation &&
				operand1.equals(operation.operand1) && operand2.equals(operation.operand2);
	}
}
