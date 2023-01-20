package x590.jdecompiler.operation;

import java.util.Objects;

import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.type.Type;

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
	public void onCastReturnType(Type newType) {
		this.returnType = Objects.requireNonNull(newType);
	}
	
	protected boolean equals(ReturnableOperation other) {
		return returnType.equals(other.returnType);
	}
}
