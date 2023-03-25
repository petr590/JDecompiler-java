package x590.jdecompiler.variable;

public interface VariableWrapper extends EmptyableVariableWrapper, Variable {
	
	public static EmptyVariableWrapper empty() {
		return EmptyVariableWrapper.INSTANCE;
	}
	
	@Override
	public default VariableWrapper nonEmpty() {
		return this;
	}
	
	@Override
	public default VariableWrapper wrapped() {
		return Variable.super.wrapped();
	}
}
