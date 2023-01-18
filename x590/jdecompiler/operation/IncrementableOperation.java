package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.invoke.ConcatStringsOperation;
import x590.jdecompiler.operation.operator.BinaryOperatorOperation;
import x590.jdecompiler.operation.operator.XorOperatorOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public interface IncrementableOperation {
	
	/** Так как интерфейс не может содержать полей,
	 * и множественное наследование не поддерживается в Java,
	 * все поля будут в этом классе. */
	public static class IncrementData {
		private Operation operatorOperand;
		private CastOperation castOperation;
		private boolean shortInc, preInc;
		private String operator;
		private Operation stringLoadOperation;

		public Operation getOperatorOperand() {
			return operatorOperand;
		}
		
		public CastOperation getCastOperation() {
			return castOperation;
		}
		
		public boolean isShortInc() {
			return shortInc;
		}
		
		public boolean isPreInc() {
			return preInc;
		}
		
		public String getOperator() {
			return operator;
		}
		
		public Operation getStringLoadOperation() {
			return stringLoadOperation;
		}
	}
	
	public static boolean canUseShortOperatorFor(Type type) {
		return type.isPrimitive() || type.isWrapperClassType();
	}
	
	public boolean isLoadOperation(Operation operation);

	public Type getReturnType();
	
	public void setReturnType(Type returnType);
	
	public default void setProbableType(Type probableType) {}
	
	
	public default IncrementData init(DecompilationContext context, Operation value, Type type) {
		
		Type returnType = PrimitiveType.VOID;
		
		IncrementData data = new IncrementData();
		
		
		if(!context.hasBreakAtCurrentIndex() && !context.stackEmpty() && context.peek().equals(value)) {
			returnType = type;
			context.pop();
			data.preInc = true;
		}
		
		if(getReturnType().canUseShortOperator()) {
			
			Operation notCastedValue;
			
			if(value instanceof CastOperation castOperation) {
				data.castOperation = castOperation;
				notCastedValue = castOperation.getOperand();
				setProbableType(castOperation.getReturnType());
				
			} else {
				notCastedValue = value;
			}
			
			
			if(notCastedValue instanceof BinaryOperatorOperation binaryOperator) {
				
				Operation operand1 = binaryOperator.operand1();
				
				if(isLoadOperation(operand1)) {
					
					data.operator = binaryOperator.getOperator();
					data.operatorOperand = binaryOperator.operand2();
					
					if(data.operator.equals("+") || data.operator.equals("-")) {
						
						data.shortInc = data.operatorOperand.isOne();
						
						if(!context.hasBreakAtCurrentIndex()) {
							
							if(!data.preInc && !context.stackEmpty() && context.peek().equals(operand1)) {
								returnType = type;
								context.pop();
								
							} else if(data.shortInc) {
								
								context.onNextPush(operation -> {
									
									if(isLoadOperation(operation)) {
										setReturnType(operation.getReturnType());
										data.preInc = true;
										
										Operation self = (Operation)this;
										context.push(self);
										context.currentScope().remove(self);
										return false;
									}
					
									return true;
								});
							}
						}
						
					} else if(data.operator.equals("^") && binaryOperator instanceof XorOperatorOperation xorOperator && xorOperator.isBitNot()) {
						data.operator = null;
						data.operatorOperand = null;
					}
				}
				
			} else if(value instanceof ConcatStringsOperation concatStrings) {
				
				Operation loadOperation = concatStrings.getFirstOperand();
				
				if(isLoadOperation(loadOperation)) {
					concatStrings.removeFirstOperand();
					concatStrings.setCanOmitEmptyStringFunc(() -> loadOperation.getReturnType().equals(ClassType.STRING));
					
					data.operator = "+";
					data.operatorOperand = concatStrings;
					data.stringLoadOperation = loadOperation;
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
		
		Operation stringLoadOperation = data.stringLoadOperation;
		
		if(operator != null && (data.castOperation == null ||
				type == PrimitiveType.VOID || type.isSubtypeOf(data.castOperation.getReturnType())) &&
				(stringLoadOperation == null || stringLoadOperation.getReturnType().equalsOneOf(ClassType.OBJECT, ClassType.STRING))) {
			
			if(data.shortInc) {
				out.print(operator).print(operator);
			} else {
				out.printsp().print(operator).printsp('=').print(data.operatorOperand, context);
			}
			
		} else if(stringLoadOperation != null) {
			out.print(" = ").print(stringLoadOperation, context).print(" + ");
			writeValue(out, context);
			
		} else {
			out.write(" = ");
			writeValue(out, context);
		}
	}
	
	public void writeName(StringifyOutputStream out, StringifyContext context);
	public void writeValue(StringifyOutputStream out, StringifyContext context);
}