package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.BooleanOperation;

public abstract class ConditionOperation extends BooleanOperation {
	
	protected boolean inverted = false;
	protected ConditionOperation() {}
	
	@Override
	public abstract void writeTo(StringifyOutputStream out, StringifyContext context);
	
	public ConditionOperation invert() {
		inverted = !inverted;
		onInvert();
		return this;
	}
	
	protected void onInvert() {}
	
	public boolean equals(ConditionOperation other) {
		return inverted == other.inverted;
	}
}
