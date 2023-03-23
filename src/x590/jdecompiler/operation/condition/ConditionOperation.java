package x590.jdecompiler.operation.condition;

import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.BooleanOperation;

public abstract class ConditionOperation extends AbstractOperation implements BooleanOperation {
	
	protected boolean inverted;
	protected ConditionOperation() {}
	
	public ConditionOperation invert() {
		inverted = !inverted;
		onInvert();
		return this;
	}
	
	protected void onInvert() {}
	
	public boolean isAlwaysTrue() {
		return false;
	}
	
	public boolean equals(ConditionOperation other) {
		return inverted == other.inverted;
	}
}
