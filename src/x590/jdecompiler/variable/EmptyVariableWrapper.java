package x590.jdecompiler.variable;

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
	public void assignName() {}
	
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
