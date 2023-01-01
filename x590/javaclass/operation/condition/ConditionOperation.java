package x590.javaclass.operation.condition;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.BooleanOperation;

public abstract class ConditionOperation extends BooleanOperation {
	
	protected boolean inverted = false;
	protected ConditionOperation() {}
	
	public abstract void writeTo(StringifyOutputStream out, StringifyContext context);
	
	public ConditionOperation invert() {
		inverted = !inverted;
		onInvert();
		return this;
	}
	
	protected void onInvert() {}
}