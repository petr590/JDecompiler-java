package x590.jdecompiler.modifiers;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class FieldModifiers extends ClassEntryModifiers {
	
	private static final Int2ObjectMap<FieldModifiers> INSTANCES = new Int2ObjectArrayMap<>();
	
	private FieldModifiers(int value) {
		super(value);
	}
	
	public static FieldModifiers of(int modifiers) {
		return INSTANCES.computeIfAbsent(modifiers, FieldModifiers::new);
	}
	
	public static FieldModifiers read(ExtendedDataInputStream in) {
		return INSTANCES.computeIfAbsent(in.readUnsignedShort(), FieldModifiers::new);
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
