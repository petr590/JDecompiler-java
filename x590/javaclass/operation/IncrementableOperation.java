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
		public Operation operatorOperand;
		public CastOperation castOperation;
		public boolean shortInc, preInc;
		public String operator;
	}
	
	
	public boolean canIncrement();
	
	public boolean isLoadOperation(Operation operation);
	
	public void setReturnType(Type returnType);
	
	public Type getReturnTypeFor(Operation operation);
	
	public default void setProbableType(Type probableType) {}
	
	
	public default IncrementData init(DecompilationContext context, Operation value, Type type) {
		
		Type returnType = PrimitiveType.VOID;
		
		IncrementData data = new IncrementData();
		
		if(canIncrement()) {
			
			Operation originalValue = value.original();
			
			Operation notCastedValue;
			
			if(originalValue instanceof CastOperation) {
				data.castOperation = (CastOperation)originalValue;
				notCastedValue = data.castOperation.getOperand();
				setProbableType(data.castOperation.getReturnType());
				
			} else {
				notCastedValue = originalValue;
			}
			
			if(!context.stack.empty() && context.stack.peek().equals(originalValue)) {
				returnType = type;
				context.stack.pop();
				data.preInc = true;
			}
			
			
			if(notCastedValue instanceof BinaryOperatorOperation binaryOperator) {
				
				Operation operand1 = binaryOperator.operand1().original();
				
				if(isLoadOperation(operand1)) {
					
					data.operator = binaryOperator.getOperator();
					data.operatorOperand = binaryOperator.operand2();
					
					if(data.operator.equals("+") || data.operator.equals("-")) {
						
						data.shortInc = data.operatorOperand.isOne();
						
						if(!data.preInc && !context.stack.empty() && context.stack.peek().equals(operand1)) {
							returnType = type;
							context.stack.pop();
							
						} else if(data.shortInc) {
							
							context.stack.onNextPush(operation -> {
								
								operation = operation.original();
								
								if(isLoadOperation(operation)) {
									setReturnType(getReturnTypeFor(operation));
									data.preInc = true;
									
									Operation self = (Operation)this;
									context.stack.push(self);
									context.currentScope().remove(self);
									return false;
								}
				
								return true;
							});
						}
						
					} else if(data.operator.equals("^") && binaryOperator instanceof XorOperatorOperation xorOperator && xorOperator.isBitNot()) {
						data.operator = null;
						data.operatorOperand = null;
					}
				}
			}
			
			if(data.operatorOperand != null)
				data.operatorOperand.allowImplicitCast();
		}
		
		setReturnType(returnType);
		
		return data;
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
