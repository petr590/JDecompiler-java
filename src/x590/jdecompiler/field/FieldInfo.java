package x590.jdecompiler.field;

import java.lang.reflect.Field;

import x590.jdecompiler.modifiers.FieldModifiers;
import x590.jdecompiler.type.ReferenceType;

public record FieldInfo(FieldDescriptor descriptor, FieldModifiers modifiers) {
	
	public static FieldInfo fromReflectField(ReferenceType declaringClass, Field field) {
		return new FieldInfo(
				FieldDescriptor.fromReflectField(declaringClass, field),
				FieldModifiers.of(field.getModifiers())
		);
	}
}
