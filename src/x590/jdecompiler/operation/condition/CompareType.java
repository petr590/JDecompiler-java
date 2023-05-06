package x590.jdecompiler.operation.condition;

import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;

public class CompareType {
	
	public static final EqualsCompareType
			EQUALS     = new EqualsCompareType("==", "!"),
			NOT_EQUALS = new EqualsCompareType("!=", "");
	
	public static final CompareType
			GREATER           = new CompareType(">"),
			GREATER_OR_EQUALS = new CompareType(">="),
			LESS              = new CompareType("<"),
			LESS_OR_EQUALS    = new CompareType("<=");
	
	static {
		((CompareType)EQUALS).invertedType = NOT_EQUALS;
		((CompareType)NOT_EQUALS).invertedType = EQUALS;
		
		GREATER.invertedType = LESS_OR_EQUALS;
		LESS_OR_EQUALS.invertedType = GREATER;
		
		LESS.invertedType = GREATER_OR_EQUALS;
		GREATER_OR_EQUALS.invertedType = LESS;
	}
	
	private final String binaryOperator;
	private CompareType invertedType;
	
	private final boolean isEqualsCompareType;
	
	private CompareType(String binaryOperator) {
		this.binaryOperator = binaryOperator;
		this.isEqualsCompareType = this instanceof EqualsCompareType;
	}
	
	public boolean isEqualsCompareType() {
		return isEqualsCompareType;
	}
	
	public String getOperator(boolean inverted) {
		return (inverted ? invertedType : this).binaryOperator;
	}
	
	public CompareType getInverted() {
		return invertedType;
	}
	
	public Type getRequiredType() {
		return Types.EXCLUDING_BOOLEAN_TYPE;
	}
	
	public int getPriority() {
		return Priority.GREATER_LESS_COMPARASION;
	}
	
	
	@Override
	public String toString() {
		return "CompareType(\"" + binaryOperator + "\")";
	}
	
	
	public static final class EqualsCompareType extends CompareType {
		
		private final String unaryOperator;
		
		private EqualsCompareType(String binaryOperator, String unaryOperator) {
			super(binaryOperator);
			this.unaryOperator = unaryOperator;
		}
		
		public String getUnaryOperator(boolean inverted) {
			return (inverted ? (EqualsCompareType)getInverted() : this).unaryOperator;
		}
		
		@Override
		public Type getRequiredType() {
			return Types.ANY_TYPE;
		}
		
		@Override
		public int getPriority() {
			return Priority.EQUALS_COMPARASION;
		}
		
		public int getUnaryPriority(boolean inverted, Operation operand) {
			return (this == EQUALS) ^ inverted ? Priority.LOGICAL_NOT : operand.getPriority();
		}
		
		
		@Override
		public String toString() {
			return "EqualsCompareType(\"" + getOperator(false) + "\", \"" + unaryOperator + "\")";
		}
	}
}
