package x590.jdecompiler.variable;

public interface VariableWrapper extends EmptyableVariableWrapper, Variable {
	
	static EmptyVariableWrapper empty() {
		return EmptyVariableWrapper.INSTANCE;
	}
	
	@Override
	default VariableWrapper nonEmpty() {
		return this;
	}
	
	@Override
	default VariableWrapper wrapped() {
		return Variable.super.wrapped();
	}
}
