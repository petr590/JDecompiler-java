package x590.jdecompiler.method;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

import x590.jdecompiler.MemberInfo;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.type.reference.RealReferenceType;

public final class MethodInfo extends MemberInfo<MethodDescriptor, MethodModifiers> {
	
	public MethodInfo(MethodDescriptor descriptor, MethodDescriptor genericDescriptor, MethodModifiers modifiers) {
		super(descriptor, genericDescriptor, modifiers);
	}
	
	
	public static MethodInfo fromReflectMethod(RealReferenceType declaringClass, Method method) {
		return new MethodInfo(
				MethodDescriptor.fromReflectMethod(declaringClass, method),
				MethodDescriptor.fromReflectMethodGeneric(declaringClass, method),
				MethodModifiers.of(method.getModifiers())
		);
	}
	
	public static MethodInfo fromReflectConstructor(RealReferenceType declaringClass, Constructor<?> constructor) {
		return new MethodInfo(
				MethodDescriptor.fromReflectConstructor(declaringClass, constructor),
				MethodDescriptor.fromReflectConstructorGeneric(declaringClass, constructor),
				MethodModifiers.of(constructor.getModifiers())
		);
	}
	
	
	@Override
	public String toString() {
		return "MethodInfo [descriptor = " + getDescriptor() + ", modifiers = " + getModifiers() + "]";
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(getDescriptor(), getModifiers());
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof MethodInfo other && this.equals(other);
	}
	
	public boolean equals(MethodInfo other) {
		return this == other ||
				getDescriptor().equals(other.getDescriptor()) &&
				getModifiers().equals(other.getModifiers());
	}
}
