package x590.jdecompiler.modifiers;

import x590.jdecompiler.util.IWhitespaceStringBuilder;
import x590.jdecompiler.util.WhitespaceStringBuilder;
import x590.util.IntegerUtil;

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
			
			ACC_MODULE       = 0x8000, // module class
			ACC_OPEN         = 0x0020, // module attribute
			ACC_MANDATED     = 0x8000, // module entry
			ACC_TRANSITIVE   = 0x0020, // module requirement
			ACC_STATIC_PHASE = 0x0040, // module requirement
			
			ACC_ACCESS_FLAGS = ACC_VISIBLE | ACC_PUBLIC | ACC_PRIVATE | ACC_PROTECTED,
			ACC_SYNTHETIC_OR_BRIDGE = ACC_SYNTHETIC | ACC_BRIDGE;
	
	
	protected final int value;
	
	public Modifiers(int value) {
		this.value = value;
	}
	
	
	public int getValue() {
		return value;
	}
	
	
	public boolean isSynthetic() {
		return (value & ACC_SYNTHETIC) != 0;
	}
	
	
	public boolean isNotSynthetic() {
		return (value & ACC_SYNTHETIC) == 0;
	}
	
	
	public boolean allOf(int modifiers) {
		return (value & modifiers) == modifiers;
	}
	
	public boolean anyOf(int modifiers) {
		return (value & modifiers) != 0;
	}
	
	public boolean notAllOf(int modifier) {
		return (value & modifier) != modifier;
	}
	
	public boolean noneOf(int modifier) {
		return (value & modifier) == 0;
	}


	public int and(int modifier) {
		return value & modifier;
	}
	
	
	public String toHex() {
		return IntegerUtil.hex4(value);
	}
	
	public String toHexWithPrefix() {
		return IntegerUtil.hex4WithPrefix(value);
	}
	
	
	protected IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return new WhitespaceStringBuilder().printTrailingSpace(forWriting)
				.appendIf(!forWriting && isSynthetic(), "synthetic");
	}
	
	public String toSimpleString() {
		return toStringBuilder(false).toString();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " { " + toStringBuilder(false).toString() + " }";
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Modifiers modifiers && this.equals(modifiers);
	}
	
	public boolean equals(Modifiers other) {
		return value == other.value;
	}
}
