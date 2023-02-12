package x590.jdecompiler.modifiers;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class ClassModifiers extends ClassEntryModifiers {
	
	public ClassModifiers(int value) {
		super(value);
	}
	
	public static ClassModifiers read(ExtendedDataInputStream in) {
		return new ClassModifiers(in.readUnsignedShort());
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
	
	public boolean isModule() {
		return (value & ACC_MODULE) != 0;
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
	
	public boolean isNotModule() {
		return (value & ACC_MODULE) == 0;
	}
	
	public boolean isNotStrictfp() {
		return (value & ACC_STRICT) == 0;
	}
	
	
	@Override
	public IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return super.toStringBuilder(forWriting)
				.appendIf(isAbstract() && isNotInterface(), "abstract")
				.appendIf(isStrictfp(), "strictfp")
				.appendIf(isInterface() && isNotAnnotation(), "interface")
				.appendIf(isAnnotation(), forWriting ? "@interface" : "annotation")
				.appendIf(isEnum(), "enum")
				.appendIf(isModule(), "module");
	}
}
