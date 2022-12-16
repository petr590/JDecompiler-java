package x590.javaclass.variable;

import x590.javaclass.scope.Scope;
import x590.javaclass.type.Type;
import x590.javaclass.type.Types;

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
}