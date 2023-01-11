package x590.jdecompiler.operation.condition;

import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;

public class EqualsCompareType extends CompareType {
	
	protected String unaryOperator;
	
	public EqualsCompareType(String binaryOperator, String unaryOperator) {
		super(binaryOperator);
		this.unaryOperator = unaryOperator;
	}
	
	public String getUnaryOperator(boolean inverted) {
		return (inverted ? (EqualsCompareType)invertedType : this).unaryOperator;
	}
	
	@Override
	public Type getRequiredType() {
		return Types.ANY_TYPE;
	}
	
	@Override
	public int getPriority() {
		return Priority.EQUALS_COMPARASION;
	}
}
