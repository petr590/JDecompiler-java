package x590.javaclass;

import x590.javaclass.type.ReferenceType;

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
