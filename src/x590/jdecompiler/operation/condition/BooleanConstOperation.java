package x590.jdecompiler.operation.condition;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;

public final class BooleanConstOperation extends ConditionOperation {
	
	public static final BooleanConstOperation
			TRUE = new BooleanConstOperation(true),
			FALSE = new BooleanConstOperation(false);
	
	private BooleanConstOperation(boolean value) {
		this.inverted = value;
	}
	
	@Override
	public ConditionOperation invert() {
		return inverted ? FALSE : TRUE;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(inverted ? "true" : "false");
	}
	
	@Override
	public boolean isAlwaysTrue() {
		return inverted;
	}
	
	@Override
	public boolean isAlwaysFalse() {
		return inverted ^ true;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
