package x590.javaclass.operation;

import java.util.Objects;

import x590.javaclass.type.Type;

public abstract class ReturnableOperation extends Operation {
	
	protected Type returnType;
	
	public ReturnableOperation(Type returnType) {
		this.returnType = Objects.requireNonNull(returnType);
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	protected void onCastReturnType(Type newType) {
		this.returnType = Objects.requireNonNull(newType);
	}
}