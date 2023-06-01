package x590.jdecompiler.field;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.lang.reflect.Field;
import java.util.Objects;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.MemberInfo;
import x590.jdecompiler.modifiers.FieldModifiers;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.RealReferenceType;
import x590.util.annotation.Nullable;

public final class FieldInfo extends MemberInfo<FieldDescriptor, FieldModifiers> {
	
	public static final Int2ObjectMap<String> UNDEFINED_ENUM_TABLE = new Int2ObjectArrayMap<>();
	
	public FieldInfo(FieldDescriptor descriptor, FieldDescriptor genericDescriptor, FieldModifiers modifiers) {
		super(descriptor, genericDescriptor, modifiers);
		setEnumTable(getEnumTable(descriptor, modifiers));
	}
	
	private static @Nullable Int2ObjectMap<String> getEnumTable(FieldDescriptor descriptor, FieldModifiers modifiers) {
		var name = descriptor.getName();
		
		return (name.startsWith("$SWITCH_TABLE$") ||
				name.startsWith("$SwitchMap$")) &&
				modifiers.allOf(ACC_STATIC | ACC_SYNTHETIC) &&
				descriptor.getType().equals(ArrayType.INT_ARRAY) ?
						new Int2ObjectArrayMap<>() :
						null;
	}
	
	
	public static FieldInfo fromReflectField(RealReferenceType declaringClass, Field field) {
		return new FieldInfo(
				FieldDescriptor.fromReflectField(declaringClass, field),
				FieldDescriptor.fromReflectFieldGeneric(declaringClass, field),
				FieldModifiers.of(field.getModifiers())
		);
	}
	
	
	@Override
	public String toString() {
		return "FieldInfo [descriptor = " + getDescriptor() + ", modifiers = " + getModifiers() + "]";
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getDescriptor(), getModifiers());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof FieldInfo other && this.equals(other);
	}
	
	public boolean equals(FieldInfo other) {
		return this == other ||
				getDescriptor().equals(other.getDescriptor()) &&
				getModifiers().equals(other.getModifiers());
	}
}
