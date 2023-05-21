package x590.jdecompiler.variable;

import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;
import x590.util.annotation.Nullable;

public class NamedVariable extends AbstractVariable {
	
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
	public String getPossibleName() {
		return name;
	}
	
	@Override
	public void addPossibleName(@Nullable String name) {
		// Do nothing
	}
	
	
	@Override
	public String toString() {
		return String.format("NamedVariable #%x { type = %s, name = %s, enclosingScope = %s }", hashCode(), type, name, getEnclosingScope());
	}
}
