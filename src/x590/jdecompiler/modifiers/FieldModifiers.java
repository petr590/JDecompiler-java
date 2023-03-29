package x590.jdecompiler.modifiers;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class FieldModifiers extends ClassEntryModifiers {
	
	public FieldModifiers(int value) {
		super(value);
	}
	
	public static FieldModifiers of(int modifiers) {
		return new FieldModifiers(modifiers);
	}
	
	public static FieldModifiers read(ExtendedDataInputStream in) {
		return new FieldModifiers(in.readUnsignedShort());
	}
	
	
	public boolean isVolatile() {
		return (value & ACC_VOLATILE) != 0;
	}
	
	public boolean isTransient() {
		return (value & ACC_TRANSIENT) != 0;
	}
	
	public boolean isEnum() {
		return (value & ACC_ENUM) != 0;
	}
	
	
	public boolean isNotVolatile() {
		return (value & ACC_VOLATILE) == 0;
	}
	
	public boolean isNotTransient() {
		return (value & ACC_TRANSIENT) == 0;
	}
	
	public boolean isNotEnum() {
		return (value & ACC_ENUM) == 0;
	}
	
	
	@Override
	public IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return super.toStringBuilder(forWriting)
				.appendIf(isVolatile(), "volatile")
				.appendIf(isTransient(), "transient")
				.appendIf(!forWriting && isEnum(), "enum");
	}
}
