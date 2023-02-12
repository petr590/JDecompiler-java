package x590.jdecompiler.modifiers;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class ModuleModifiers extends ModuleEntryModifiers {
	
	public ModuleModifiers(int value) {
		super(value);
	}
	
	public static ModuleModifiers read(ExtendedDataInputStream in) {
		return new ModuleModifiers(in.readUnsignedShort());
	}
	
	
	public boolean isOpen() {
		return (value & ACC_OPEN) != 0;
	}
	
	public boolean isNotOpen() {
		return (value & ACC_OPEN) == 0;
	}
	
	
	@Override
	public IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return super.toStringBuilder(forWriting)
				.appendIf(isOpen(), "open");
	}
}
