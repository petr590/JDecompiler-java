package x590.jdecompiler.modifiers;

import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public final class FieldModifiers extends Modifiers {
	
	public FieldModifiers(int value) {
		super(value);
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
	public String toString() {
		IWhitespaceStringBuilder str = toStringBuilder();
		
		if(isVolatile()) str.append("volatile");
		if(isTransient()) str.append("transient");
		if(isEnum()) str.append("enum");
		
		return str.toString();
	}
}
