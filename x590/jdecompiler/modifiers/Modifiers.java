package x590.jdecompiler.modifiers;

import x590.jdecompiler.util.IWhitespaceStringBuilder;
import x590.jdecompiler.util.WhitespaceStringBuilder;
import x590.util.Util;

public abstract class Modifiers {
	
	public static final int
			ACC_NONE         = 0x0000,
			ACC_VISIBLE      = 0x0000, // class, field, method
			ACC_PUBLIC       = 0x0001, // class, field, method
			ACC_PRIVATE      = 0x0002, // nested class, field, method
			ACC_PROTECTED    = 0x0004, // nested class, field, method
			ACC_STATIC       = 0x0008, // nested class, field, method
			ACC_FINAL        = 0x0010, // class, field, method
			ACC_SYNCHRONIZED = 0x0020, // method
			ACC_SUPER        = 0x0020, // class (deprecated)
			ACC_VOLATILE     = 0x0040, // field
			ACC_TRANSIENT    = 0x0080, // field
			ACC_BRIDGE       = 0x0040, // method
			ACC_VARARGS      = 0x0080, // method
			ACC_NATIVE       = 0x0100, // method
			ACC_INTERFACE    = 0x0200, // class
			ACC_ABSTRACT     = 0x0400, // class, method
			ACC_STRICT       = 0x0800, // class, method
			ACC_SYNTHETIC    = 0x1000, // class, field, method
			ACC_ANNOTATION   = 0x2000, // class
			ACC_ENUM         = 0x4000, // class, field
			
			ACC_ACCESS_FLAGS = ACC_VISIBLE | ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED,
			ACC_SYNTHETIC_OR_BRIDGE = ACC_SYNTHETIC | ACC_BRIDGE;
	
	
	final int value;
	
	public Modifiers(int value) {
		this.value = value;
	}
	
	
	public int getValue() {
		return value;
	}
	
	
	public boolean isPublic() {
		return (value & ACC_PUBLIC) != 0;
	}
	
	public boolean isPrivate() {
		return (value & ACC_PRIVATE) != 0;
	}
	
	public boolean isProtected() {
		return (value & ACC_PROTECTED) != 0;
	}
	
	
	public boolean isStatic() {
		return (value & ACC_STATIC) != 0;
	}
	
	public boolean isFinal() {
		return (value & ACC_FINAL) != 0;
	}
	
	
	public boolean isSynthetic() {
		return (value & ACC_SYNTHETIC) != 0;
	}
	
	
	public boolean isNotPublic() {
		return (value & ACC_PUBLIC) == 0;
	}
	
	public boolean isNotPrivate() {
		return (value & ACC_PRIVATE) == 0;
	}
	
	public boolean isNotProtected() {
		return (value & ACC_PROTECTED) == 0;
	}
	
	
	public boolean isNotStatic() {
		return (value & ACC_STATIC) == 0;
	}
	
	public boolean isNotFinal() {
		return (value & ACC_FINAL) == 0;
	}
	
	
	public boolean isNotSynthetic() {
		return (value & ACC_SYNTHETIC) == 0;
	}

	
	public boolean isAll(int modifiers) {
		return (value & modifiers) == modifiers;
	}

	public boolean isAny(int modifiers) {
		return (value & modifiers) != 0;
	}
	
	public boolean isNotAll(int modifier) {
		return (value & modifier) != modifier;
	}
	
	public boolean isNotAny(int modifier) {
		return (value & modifier) == 0;
	}
	
	public int and(int modifier) {
		return value & modifier;
	}
	
	
	public String toHex() {
		return Util.hex4(value);
	}
	
	public String toHexWithPrefix() {
		return Util.hex4WithPrefix(value);
	}
	
	
	IWhitespaceStringBuilder toStringBuilder() {
		IWhitespaceStringBuilder str = new WhitespaceStringBuilder();
		
		if(isPublic()) str.append("public");
		if(isPrivate()) str.append("private");
		if(isProtected()) str.append("protected");
		if(isStatic()) str.append("static");
		if(isFinal()) str.append("final");
		if(isSynthetic()) str.append("synthetic");
		
		return str;
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Modifiers modifiers && this.equals(modifiers);
	}
	
	public boolean equals(Modifiers other) {
		return this.value == other.value;
	}
}
