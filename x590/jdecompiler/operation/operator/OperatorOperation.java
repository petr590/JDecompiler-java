package x590.jdecompiler.operation.operator;

import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.type.Type;

public abstract class OperatorOperation extends ReturnableOperation {
	
	public OperatorOperation(Type returnType) {
		super(returnType);
	}
	
	public abstract String getOperator();
	
	@Override
	public abstract int getPriority();
}
