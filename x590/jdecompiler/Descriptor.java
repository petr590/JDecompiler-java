package x590.jdecompiler;

import x590.jdecompiler.type.ReferenceType;

public abstract class Descriptor {
	
	private final ReferenceType clazz;
	private final String name;
	
	public Descriptor(ReferenceType clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}
	
	@Override
	public abstract String toString();
	
	public ReferenceType getDeclaringClass() {
		return clazz;
	}
	
	public String getName() {
		return name;
	}
}
