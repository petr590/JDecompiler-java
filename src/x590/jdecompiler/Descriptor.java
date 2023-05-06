package x590.jdecompiler;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.type.reference.RealReferenceType;
import x590.jdecompiler.writable.DisassemblingWritable;

public abstract class Descriptor implements DisassemblingWritable<ClassInfo> {
	
	private final RealReferenceType declaringClass;
	private final String name;
	
	public Descriptor(RealReferenceType clazz, String name) {
		this.declaringClass = clazz;
		this.name = name;
	}
	
	@Override
	public abstract String toString();
	
	public RealReferenceType getDeclaringClass() {
		return declaringClass;
	}
	
	public String getName() {
		return name;
	}
}
