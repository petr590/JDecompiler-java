package x590.jdecompiler.variable;

import x590.util.annotation.Nullable;

public final class EmptyVariableWrapper implements EmptyableVariableWrapper {
	
	public static final EmptyVariableWrapper INSTANCE = new EmptyVariableWrapper();
	
	
	private EmptyVariableWrapper() {}
	
	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public VariableWrapper nonEmpty() {
		throw new UnsupportedOperationException("Variable is empty");
	}
	
	@Override
	public boolean hasName() {
		return false;
	}

	@Override
	public void assignName() {}

	@Override
	public @Nullable String getName() {
		throw new UnsupportedOperationException("Empty variable has not name");
	}

	@Override
	public void reduceType() {}

	@Override
	public VariableWrapper assign(Variable other) {
		return new WrappedVariable(other);
	}
	
	
	@Override
	public String toString() {
		return "EmptyVariableWrapper";
	}
}
