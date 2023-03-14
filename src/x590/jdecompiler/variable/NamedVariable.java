package x590.jdecompiler.variable;

import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;

public class NamedVariable extends Variable {
	
	private final String name;
	
	public NamedVariable(String name, Scope enclosingScope) {
		this(name, enclosingScope, Types.ANY_TYPE, false);
	}
	
	public NamedVariable(String name, Scope enclosingScope, Type type) {
		this(name, enclosingScope, type, false);
	}
	
	public NamedVariable(String name, Scope enclosingScope, boolean typeFixed) {
		this(name, enclosingScope, Types.ANY_TYPE, typeFixed);
	}
	
	public NamedVariable(String name, Scope enclosingScope, Type type, boolean typeFixed) {
		super(enclosingScope, type, typeFixed);
		this.name = name;
	}
	
	@Override
	protected String chooseName() {
		return name;
	}
	
	@Override
	public void addName(String name) {}
	
	
	@Override
	public String toString() {
		return String.format("NamedVariable { type = %s, name = %s, enclosingScope = %s }", type, name, getEnclosingScope());
	}
}
