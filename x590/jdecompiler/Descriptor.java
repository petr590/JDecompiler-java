package x590.jdecompiler;

import x590.jdecompiler.type.ReferenceType;

public abstract class Descriptor {
	
	public final ReferenceType clazz;
	public final String name;
	
	public Descriptor(ReferenceType clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}
	
	@Override
	public abstract String toString();
}
