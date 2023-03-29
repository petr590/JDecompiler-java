package x590.jdecompiler.clazz;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Интерфейс ClassInfo. Позволяет создавать экземпляры IClassInfo
 * не только из JavaClass, но и из обычного объекта Class
 */
public interface IClassInfo {
	
	public ClassModifiers getModifiers();
	
	public ReferenceType getThisType();
	
	public @Nullable ClassType getSuperType();
	
	public @Nullable @Immutable List<ClassType> getInterfaces();
	
	
	public default boolean hasField(FieldDescriptor descriptor) {
		return hasFieldByDescriptor(descriptor::equals);
	}
	
	public default boolean hasMethod(MethodDescriptor descriptor) {
		return hasMethodByDescriptor(descriptor::equals);
	}
	
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate);
	
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate);
	
	
	public Optional<FieldInfo> findFieldInfo(FieldDescriptor descriptor);
	
	public Optional<MethodInfo> findMethodInfo(MethodDescriptor descriptor);
}
