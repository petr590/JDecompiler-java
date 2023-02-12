package x590.jdecompiler.modifiers;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class MethodModifiers extends ClassEntryModifiers {
	
	public MethodModifiers(int value) {
		super(value);
	}
	
	public static MethodModifiers read(ExtendedDataInputStream in) {
		return new MethodModifiers(in.readUnsignedShort());
	}
	
	
	public boolean isAbstract() {
		return (value & ACC_ABSTRACT) != 0;
	}
	
	public boolean isNative() {
		return (value & ACC_NATIVE) != 0;
	}
	
	public boolean isSynchronized() {
		return (value & ACC_SYNCHRONIZED) != 0;
	}
	
	public boolean isBridge() {
		return (value & ACC_BRIDGE) != 0;
	}
	
	public boolean isSyntheticOrBridge() {
		return (value & ACC_SYNTHETIC_OR_BRIDGE) != 0;
	}
	
	public boolean isVarargs() {
		return (value & ACC_VARARGS) != 0;
	}
	
	public boolean isStrictfp() {
		return (value & ACC_STRICT) != 0;
	}
	
	
	public boolean isNotAbstract() {
		return (value & ACC_ABSTRACT) == 0;
	}
	
	public boolean isNotNative() {
		return (value & ACC_NATIVE) == 0;
	}
	
	public boolean isNotSynchronized() {
		return (value & ACC_SYNCHRONIZED) == 0;
	}
	
	public boolean isNotBridge() {
		return (value & ACC_BRIDGE) == 0;
	}
	
	public boolean isNotSyntheticOrBridge() {
		return (value & ACC_SYNTHETIC_OR_BRIDGE) == 0;
	}
	
	public boolean isNotVarargs() {
		return (value & ACC_VARARGS) == 0;
	}
	
	public boolean isNotStrictfp() {
		return (value & ACC_STRICT) == 0;
	}
	
	
	@Override
	public IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return super.toStringBuilder(forWriting)
				.appendIf(isAbstract(), "abstract")
				.appendIf(isNative(), "native")
				.appendIf(isSynchronized(), "synchronized")
				.appendIf(!forWriting && isBridge(), "bridge")
				.appendIf(!forWriting && isVarargs(), "varargs")
				.appendIf(isStrictfp(), "strictfp");
	}
}
