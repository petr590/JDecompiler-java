package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.operator.BinaryOperatorOperation;
import x590.javaclass.operation.operator.XorOperatorOperation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public interface IncrementableOperation {
	
	/** Так как интерфейс не может содержать полей,
	 * и множественное наследование не поддерживается в Java,
	 * все поля будут в этом классе. */
	public static class IncrementData {
		public Type returnType;
		public Operation operatorOperand;
		public CastOperation castOperation;
		public boolean shortInc, preInc;
		public String operator;
		
		public IncrementData(Type returnType, Operation operatorOperand, CastOperation castOperation, boolean shortInc, boolean preInc, String operator) {
			this.returnType = returnType;
			this.operatorOperand = operatorOperand;
			this.castOperation = castOperation;
			this.shortInc = shortInc;
			this.preInc = preInc;
			this.operator = operator;
		}
	}
	
	
	public boolean canIncrement();
	
	public boolean isLoadOperation(Operation operation);
	
	public default void setProbableType(Type probableType) {}
	
	
	public default IncrementData init(DecompilationContext context, Operation value, Type type) {
		
		Type returnType = PrimitiveType.VOID;
		Operation operatorOperand = null;
		CastOperation castOperation = null;
		boolean shortInc = false, preInc = false;
		String operator = null;
		
		if(canIncrement()) {
			
			Operation originalValue = value.original();
			
			Operation notCastedValue;
			
			if(originalValue instanceof CastOperation) {
				castOperation = (CastOperation)originalValue;
				notCastedValue = castOperation.getOperand();
				setProbableType(castOperation.getReturnType());
				
			} else {
				notCastedValue = originalValue;
			}
			
			if(!context.stack.empty() && context.stack.peek() == originalValue) {
				returnType = type;
				context.stack.pop();
				preInc = true;
			}
			
			
			if(notCastedValue instanceof BinaryOperatorOperation binaryOperator) {
				
				Operation operand1 = binaryOperator.operand1().original();
				
				if(isLoadOperation(operand1)) {
					
					operator = binaryOperator.getOperator();
					operatorOperand = binaryOperator.operand2();
					
					if((operator == "+" || operator == "-")) {
						
						shortInc = operatorOperand.isOne();
						
						
						if(!preInc && !context.stack.empty() && context.stack.peek() == operand1) {
							returnType = type;
							context.stack.pop();
						}
						
						
					} else if(operator == "^" && binaryOperator instanceof XorOperatorOperation xorOperator && xorOperator.isBitNot()) {
						operator = null;
						operatorOperand = null;
					}
				}
			}
			
			if(operatorOperand != null)
				operatorOperand.allowImplicitCast();
		}
		
		return new IncrementData(returnType, operatorOperand, castOperation, shortInc, preInc, operator);
	}
	
	
	public default void writeTo(StringifyOutputStream out, StringifyContext context, Type type, IncrementData data) {
		String operator = data.operator;
		
		if(operator != null && data.shortInc && data.preInc) {
			out.print(operator).print(operator);
			writeName(out, context);
			return;
		}
		
		writeName(out, context);
		
		if(operator != null && (data.castOperation == null ||
				type == PrimitiveType.VOID || type.isSubtypeOf(data.castOperation.getReturnType()))) {
			
			if(data.shortInc) {
				out.print(operator).print(operator);
			} else {
				out.printsp().print(operator).printsp('=').print(data.operatorOperand, context);
			}
			
		} else {
			out.write(" = ");
			writeValue(out, context);
		}
	}
	
	public void writeName(StringifyOutputStream out, StringifyContext context);
	public void writeValue(StringifyOutputStream out, StringifyContext context);
}
