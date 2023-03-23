package x590.jdecompiler.variable;

public interface EmptyableVariableWrapper extends EmptyableVariable {
	
	@Override
	public VariableWrapper nonEmpty();
	
	public void makeSame(EmptyableVariable other);
	
	@Override
	public default EmptyableVariableWrapper wrapped() {
		return this;
	}
}
