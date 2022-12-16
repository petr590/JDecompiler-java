package x590.javaclass.operation.operator;

import x590.javaclass.operation.ReturnableOperation;
import x590.javaclass.type.Type;

public abstract class OperatorOperation extends ReturnableOperation {
	
	public OperatorOperation(Type returnType) {
		super(returnType);
	}
	
	public abstract String getOperator();
	
	@Override
	public abstract int getPriority();
}