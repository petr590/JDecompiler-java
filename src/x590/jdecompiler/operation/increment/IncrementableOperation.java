package x590.jdecompiler.operation.increment;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.cast.CastOperation;
import x590.jdecompiler.operation.invoke.ConcatStringsOperation;
import x590.jdecompiler.operation.operator.BinaryOperatorOperation;
import x590.jdecompiler.operation.operator.XorOperatorOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.annotation.Nullable;

public interface IncrementableOperation extends Operation {
	
	/** Так как интерфейс не может содержать полей,
	 * и множественное наследование не поддерживается в Java,
	 * все поля будут в этом классе. */
	class IncrementData {

		private @Nullable Operation operatorOperand;
		private @Nullable CastOperation castOperation;
		private @Nullable Operation preIncLoadOperation;
		private boolean shortInc;
		private String operator;
		private @Nullable Operation stringLoadOperation;

		private IncrementData() {}

		public IncrementData(@Nullable Operation operatorOperand, @Nullable CastOperation castOperation,
							 @Nullable Operation preIncLoadOperation, boolean shortInc,
							 String operator, @Nullable Operation stringLoadOperation) {

			this.operatorOperand = operatorOperand;
			this.castOperation = castOperation;
			this.preIncLoadOperation = preIncLoadOperation;
			this.shortInc = shortInc;
			this.operator = operator;
			this.stringLoadOperation = stringLoadOperation;
		}

		public Operation getOperatorOperand() {
			return operatorOperand;
		}
		
		public CastOperation getCastOperation() {
			return castOperation;
		}
		
		public Operation getPreIncLoadOperation() {
			return preIncLoadOperation;
		}
		
		public boolean isShortInc() {
			return shortInc;
		}
		
		public boolean isPreInc() {
			return getPreIncLoadOperation() != null;
		}
		
		public String getOperator() {
			return operator;
		}
		
		public Operation getStringLoadOperation() {
			return stringLoadOperation;
		}

		public IncrementData inline(Int2ObjectMap<Operation> varTable) {
			var operatorOperand     = this.operatorOperand     == null ? null : this.operatorOperand.inline(varTable);
			var castOperation       = this.castOperation       == null ? null : this.castOperation.inline(varTable);
			var preIncLoadOperation = this.preIncLoadOperation == null ? null : this.preIncLoadOperation.inline(varTable);
			var stringLoadOperation = this.stringLoadOperation == null ? null : this.stringLoadOperation.inline(varTable);

			return
					this.operatorOperand     == operatorOperand &&
					this.castOperation       == castOperation &&
					this.preIncLoadOperation == preIncLoadOperation &&
					this.stringLoadOperation == stringLoadOperation ?
							this :
							new IncrementData(operatorOperand, (CastOperation) castOperation,
									preIncLoadOperation, shortInc, operator, stringLoadOperation);
		}
	}
	
	static boolean canUseShortOperatorFor(Type type) {
		return type.isPrimitive() || type.isWrapperClassType();
	}
	
	boolean isLoadOperation(Operation operation);
	
	void setReturnType(Type returnType);
	
	default void setProbableType(Type probableType) {}

	IncrementData getIncData();

	void setIncData(IncrementData incData);
	
	
	default IncrementData init(DecompilationContext context, Operation value, Type type) {
		
		Type returnType = PrimitiveType.VOID;
		
		IncrementData data = new IncrementData();
		
		
		if(!context.hasBreakAtCurrentIndex() && !context.stackEmpty()) {
			
			Operation loadOperation = context.peek();
			
			if(loadOperation.equals(value)) {
				returnType = type;
				context.pop();
				data.preIncLoadOperation = loadOperation;
			}
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
							
							if(!data.isPreInc() && !context.stackEmpty() && context.peek().equals(operand1)) {
								returnType = type;
								context.pop();
								
							} else if(data.shortInc) {
								
								context.onNextPush(operation -> {
									
									if(isLoadOperation(operation)) {
										setReturnType(operation.getReturnType());
										data.preIncLoadOperation = operation;
										
										context.push(this);
										context.currentScope().remove(this);
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
	
	
	default void writeTo(StringifyOutputStream out, StringifyContext context, Type type, IncrementData data) {
		String operator = data.operator;
		
		if(operator != null && data.shortInc && data.isPreInc()) {
			out.print(operator).print(operator);
			writeName(out, context);
			return;
		}
		
		writeName(out, context);

		Operation castOperation = data.castOperation;
		Operation stringLoadOperation = data.stringLoadOperation;
		
		if(operator != null &&
			(castOperation == null || type == PrimitiveType.VOID || type.canCastToNarrowest(castOperation.getReturnType())) &&
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
			out.print(" = ");
			writeValue(out, context);
		}
	}
	
	void writeName(StringifyOutputStream out, StringifyContext context);
	void writeValue(StringifyOutputStream out, StringifyContext context);


	@Override
	default Operation inline(Int2ObjectMap<Operation> varTable) {
		var inlined = (IncrementableOperation) Operation.super.inline(varTable);

		if(inlined != this) {
			inlined.setIncData(getIncData().inline(varTable));
		}

		return inlined;
	}
}
