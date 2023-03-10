package x590.jdecompiler;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class PlainClassInfo implements IClassInfo {
	
	private final ClassModifiers modifiers;
	private final ReferenceType thisType;
	private final @Nullable ClassType superType;
	private final @Nullable @Immutable List<ClassType> interfaces;
	private final @Immutable List<FieldDescriptor> fieldDescriptors;
	private final @Immutable List<MethodDescriptor> methodDescriptors;
	
	private PlainClassInfo(ReferenceType thisType, Class<?> clazz) {
		this.modifiers = ClassModifiers.of(clazz.getModifiers());
		this.thisType = thisType;
		this.superType = thisType.getSuperType();
		this.interfaces = thisType.getInterfaces();
		
		this.fieldDescriptors = Arrays.stream(clazz.getDeclaredFields())
				.map(field -> FieldDescriptor.fromReflectField(thisType, field)).toList();
		
		this.methodDescriptors = Arrays.stream(clazz.getDeclaredMethods())
				.map(method -> MethodDescriptor.fromReflectMethod(thisType, method)).toList();
	}
	
	static @Nullable PlainClassInfo fromClassType(ReferenceType thisType) {
		Class<?> clazz = thisType.getClassInstance();
		return clazz != null ? new PlainClassInfo(thisType, clazz) : null;
	}
	
	@Override
	public ClassModifiers getModifiers() {
		return modifiers;
	}
	
	@Override
	public ReferenceType getThisType() {
		return thisType;
	}
	
	@Override
	public @Nullable ClassType getSuperType() {
		return superType;
	}
	
	@Override
	public @Nullable @Immutable List<ClassType> getInterfaces() {
		return interfaces;
	}
	
	@Override
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate) {
		return fieldDescriptors.stream().anyMatch(predicate);
	}
	
	@Override
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate) {
		return methodDescriptors.stream().anyMatch(predicate);
	}
}
