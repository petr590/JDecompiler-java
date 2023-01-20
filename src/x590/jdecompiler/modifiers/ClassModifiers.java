package x590.jdecompiler.modifiers;

import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class ClassModifiers extends Modifiers {
	
	public ClassModifiers(int value) {
		super(value);
	}
	
	
	public boolean isAbstract() {
		return (value & ACC_ABSTRACT) != 0;
	}
	
	public boolean isInterface() {
		return (value & ACC_INTERFACE) != 0;
	}
	
	public boolean isAnnotation() {
		return (value & ACC_ANNOTATION) != 0;
	}
	
	public boolean isEnum() {
		return (value & ACC_ENUM) != 0;
	}
	
	public boolean isStrictfp() {
		return (value & ACC_STRICT) != 0;
	}
	
	
	public boolean isNotAbstract() {
		return (value & ACC_ABSTRACT) == 0;
	}
	
	public boolean isNotInterface() {
		return (value & ACC_INTERFACE) == 0;
	}
	
	public boolean isNotAnnotation() {
		return (value & ACC_ANNOTATION) == 0;
	}
	
	public boolean isNotEnum() {
		return (value & ACC_ENUM) == 0;
	}
	
	public boolean isNotStrictfp() {
		return (value & ACC_STRICT) == 0;
	}
	
	
	@Override
	public String toString() {
		IWhitespaceStringBuilder str = toStringBuilder();
		
		if(isAbstract()) str.append("abstract");
		if(isInterface()) str.append("interface");
		if(isAnnotation()) str.append("annotation");
		if(isEnum()) str.append("enum");
		if(isStrictfp()) str.append("strictfp");
		
		return str.toString();
	}
}
