package x590.javaclass;

import x590.javaclass.type.Type;
import x590.javaclass.type.Types;

public class NamedVariable extends Variable {
	
	private final String name;
	
	public NamedVariable(String name) {
		this(name, Types.ANY_TYPE);
	}
	
	public NamedVariable(String name, Type type) {
		super(type);
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void addName(String name) {}
}