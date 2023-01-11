package x590.jdecompiler.operation.condition;

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
		EQUALS.invertedType = NOT_EQUALS;
		NOT_EQUALS.invertedType = EQUALS;
		
		GREATER.invertedType = LESS_OR_EQUALS;
		LESS_OR_EQUALS.invertedType = GREATER;
		
		LESS.invertedType = GREATER_OR_EQUALS;
		GREATER_OR_EQUALS.invertedType = LESS;
	}
	
	protected final String binaryOperator;
	protected CompareType invertedType;
	
	public final boolean isEqualsCompareType;
	
	public CompareType(String binaryOperator) {
		this.binaryOperator = binaryOperator;
		this.isEqualsCompareType = this instanceof EqualsCompareType;
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
}
