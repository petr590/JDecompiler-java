package x590.javaclass;

import x590.javaclass.type.ClassType;

public abstract class Descriptor {
	
	public final ClassType clazz;
	public final String name;
	
	public Descriptor(ClassType clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}
	
	@Override
	public abstract String toString();
}
