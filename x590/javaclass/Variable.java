package x590.javaclass;

import x590.javaclass.type.Type;
import x590.javaclass.type.Types;

public abstract class Variable {
	
	protected Type type;
	
	public Variable() {
		this(Types.ANY_TYPE);
	}
	
	public Variable(Type type) {
		this.type = type;
	}
	
	public abstract String getName();
	
	public abstract void addName(String name);
	
	public Type getType() {
		return type;
	}
	
	public void castTypeToNarrowest(Type newType) {
		type = type.castToNarrowest(newType);
	}
	
	public void castTypeToWidest(Type newType) {
		type = type.castToWidest(newType);
	}
}