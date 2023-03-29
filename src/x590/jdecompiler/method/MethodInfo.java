package x590.jdecompiler.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.type.ReferenceType;

public record MethodInfo(MethodDescriptor descriptor, MethodModifiers modifiers) {
	
	public static MethodInfo fromReflectMethod(ReferenceType declaringClass, Method method) {
		return new MethodInfo(
				MethodDescriptor.fromReflectMethod(declaringClass, method),
				MethodModifiers.of(method.getModifiers())
		);
	}
	
	public static MethodInfo fromReflectConstructor(ReferenceType declaringClass, Constructor<?> constructor) {
		return new MethodInfo(
				MethodDescriptor.fromReflectConstructor(declaringClass, constructor),
				MethodModifiers.of(constructor.getModifiers())
		);
	}
}
