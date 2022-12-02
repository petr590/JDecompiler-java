package x590.javaclass.operation.compare;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.CmpOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class CompareBinaryOperation extends CompareOperation {
	
	private final Operation operand1, operand2;
	
	protected void castOperandsTo(Type requiredType) {
		operand1.castReturnTypeToNarrowest(requiredType);
		operand2.castReturnTypeToNarrowest(requiredType);
		
		if(!compareType.isEqualsCompareType) {
			operand1.allowImplicitCast();
			operand2.allowImplicitCast();
		}
		
		Type generalType = operand1.getReturnType().castToNarrowestNoexcept(operand2.getReturnType());
		
		if(generalType == null) {
			generalType = operand2.getReturnType().castToNarrowestNoexcept(operand1.getReturnType());
			
			if(generalType == null)
				throw new DecompilationException("Incopatible types for operator " + compareType.getOperator(false) +
						": " + operand1.getReturnType() + " and " + operand2.getReturnType());
		}
		
		operand1.castReturnTypeToNarrowest(generalType);
		operand2.castReturnTypeToNarrowest(generalType);
		
		if(generalType.isPrimitive() && (generalType == PrimitiveType.LONG || generalType == PrimitiveType.FLOAT || generalType == PrimitiveType.DOUBLE)) {
			boolean allowedImplCast1 = operand1.implicitCastAllowed(),
					allowedImplCast2 = operand2.implicitCastAllowed();
			     
			if(allowedImplCast1 ^ allowedImplCast2) { // Allow cast when only one of types can be casted implicit
				(allowedImplCast1 ? operand1 : operand2).allowImplicitCast();
			}
		}
	}

	public CompareBinaryOperation(CmpOperation cmpOperation, CompareType compareType) {
		super(compareType);
		this.operand2 = cmpOperation.operand2;
		this.operand1 = cmpOperation.operand1;
		castOperandsTo(compareType.getRequiredType());
	}
	
	public CompareBinaryOperation(DecompilationContext context, Type requiredType, CompareType compareType) {
		/* We don't delegate constructor because of undefined order of initialization of the function arguments
		   which is important in this case */
		super(compareType);
		this.operand2 = context.stack.pop();
		this.operand1 = context.stack.pop();
		castOperandsTo(compareType.getRequiredType().castToNarrowest(requiredType));
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, operand1, context, Associativity.LEFT).printsp().print(compareType.getOperator(inverted)).printsp().printPrioritied(this, operand2, context, Associativity.RIGHT);
	}
}