package x590.javaclass.operation;

import x590.javaclass.type.Type;

public abstract class OperatorOperation extends ReturnableOperation {
	
	public OperatorOperation(Type returnType) {
		super(returnType);
	}
	
	protected abstract String getOperator();
	
	@Override
	public abstract int getPriority();
}