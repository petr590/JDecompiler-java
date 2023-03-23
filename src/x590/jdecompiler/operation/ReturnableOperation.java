package x590.jdecompiler.operation;

import java.util.Objects;

import x590.jdecompiler.type.Type;

public abstract class ReturnableOperation extends AbstractOperation {
	
	protected Type returnType;
	
	public ReturnableOperation(Type returnType) {
		this.returnType = Objects.requireNonNull(returnType);
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		this.returnType = Objects.requireNonNull(newType);
	}
	
	@Override
	public void reduceType() {
		this.returnType = returnType.reduced();
	}
	
	protected boolean equals(ReturnableOperation other) {
		return returnType.equals(other.returnType);
	}
}
