package x590.jdecompiler.operation;

import java.util.Objects;

import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;

/**
 * Операция с определённым возвращаемым значением, которое может меняться
 */
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
	protected void onCastReturnType(Type newType, CastingKind casting) {
		this.returnType = Objects.requireNonNull(newType);
	}
	
	
	@Override
	public final boolean deduceType() {
		var returnType = this.returnType;
		Type deducedType = getDeducedType(returnType);
		
		if(returnType != deducedType) {
			this.returnType = deducedType;
			return true;
		}
		
		return false;
	}
	
	protected Type getDeducedType(Type returnType) {
		return returnType;
	}
	
	@Override
	public void reduceType() {
		this.returnType = returnType.reduced();
	}
	
	protected boolean equals(ReturnableOperation other) {
		return returnType.equals(other.returnType);
	}
}
