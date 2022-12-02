package x590.javaclass.operation.compare;

import x590.javaclass.operation.Priority;
import x590.javaclass.type.Type;
import x590.javaclass.type.Types;

public class EqualsCompareType extends CompareType {
	
	protected String unaryOperator;
	
	public EqualsCompareType(String binaryOperator, String unaryOperator) {
		super(binaryOperator);
		this.unaryOperator = unaryOperator;
	}
	
	public String getUnaryOperator(boolean inverted) {
		return inverted ? ((EqualsCompareType)invertedType).unaryOperator : unaryOperator;
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