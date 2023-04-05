package x590.jdecompiler.modifiers;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class ModuleRequirementModifiers extends ModuleEntryModifiers {
	
	private static final Int2ObjectMap<ModuleRequirementModifiers> INSTANCES = new Int2ObjectArrayMap<>();
	
	private ModuleRequirementModifiers(int value) {
		super(value);
	}
	
	public static ModuleRequirementModifiers of(int modifiers) {
		return INSTANCES.computeIfAbsent(modifiers, ModuleRequirementModifiers::new);
	}
	
	public static ModuleRequirementModifiers read(ExtendedDataInputStream in) {
		return INSTANCES.computeIfAbsent(in.readUnsignedShort(), ModuleRequirementModifiers::new);
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
