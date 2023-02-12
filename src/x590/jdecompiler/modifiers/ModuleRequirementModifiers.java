package x590.jdecompiler.modifiers;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class ModuleRequirementModifiers extends ModuleEntryModifiers {
	
	public ModuleRequirementModifiers(int value) {
		super(value);
	}
	
	public static ModuleRequirementModifiers read(ExtendedDataInputStream in) {
		return new ModuleRequirementModifiers(in.readUnsignedShort());
	}
	
	
	public boolean isTransitive() {
		return (value & ACC_TRANSITIVE) != 0;
	}
	
	public boolean isStaticPhase() {
		return (value & ACC_STATIC_PHASE) != 0;
	}
	
	
	public boolean isNotTransitive() {
		return (value & ACC_TRANSITIVE) == 0;
	}
	
	public boolean isNotStaticPhase() {
		return (value & ACC_STATIC_PHASE) == 0;
	}
	
	
	@Override
	public IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return super.toStringBuilder(forWriting)
				.appendIf(isTransitive(), "transitive")
				.appendIf(isStaticPhase(), "static");
	}
}
