package x590.javaclass.operation.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.Priority;
import x590.javaclass.operation.ReturnableOperation;
import x590.javaclass.operation.condition.ConditionOperation;
import x590.javaclass.operation.constant.IConstOperation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class TernaryOperatorOperation extends ReturnableOperation {
	
	private final ConditionOperation condition;
	private final Operation operand1, operand2;
	
	public TernaryOperatorOperation(ConditionOperation condition, DecompilationContext context) {
		super(PrimitiveType.VOID);
		this.condition = condition;
		this.operand2 = context.stack.pop();
		this.operand1 = context.stack.pop();
		
		returnType = operand1.getReturnTypeAsGeneralNarrowest(operand2);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(returnType.isSubtypeOf(PrimitiveType.BOOLEAN) && operand1 instanceof IConstOperation iconst1 && operand2 instanceof IConstOperation iconst2) {
			
			if(iconst1.getValue() == 1 && iconst2.getValue() == 0) {
				out.print(condition, context);
			} else if(iconst1.getValue() == 0 && iconst2.getValue() == 1) {
				out.print('!').printPrioritied(this, condition, context, Priority.LOGICAL_NOT, Associativity.RIGHT);
			}
			
		} else {
			out.print(condition, context).print(" ? ")
				.printPrioritied(this, operand1, context, Associativity.LEFT).print(" : ")
				.printPrioritied(this, operand2, context, Associativity.RIGHT);
		}
	}
	
	@Override
	public int getPriority() {
		return Priority.TERNARY_OPERATOR;
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		super.onCastReturnType(newType);
		operand1.castReturnTypeToNarrowest(returnType);
		operand2.castReturnTypeToNarrowest(returnType);
	}
}