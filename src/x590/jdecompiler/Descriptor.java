package x590.jdecompiler;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.type.ReferenceType;

public abstract class Descriptor implements DisassemblingWritable<ClassInfo> {
	
	private final ReferenceType declaringClass;
	private final String name;
	
	public Descriptor(ReferenceType clazz, String name) {
		this.declaringClass = clazz;
		this.name = name;
	}
	
	@Override
	public abstract String toString();
	
	public ReferenceType getDeclaringClass() {
		return declaringClass;
	}
	
	public String getName() {
		return name;
	}
}
