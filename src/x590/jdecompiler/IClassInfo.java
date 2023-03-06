package x590.jdecompiler;

import java.util.List;
import java.util.function.Predicate;

import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public interface IClassInfo {
	
	public ClassModifiers getModifiers();
	
	public ReferenceType getThisType();
	
	public @Nullable ClassType getSuperType();
	
	public @Nullable @Immutable List<ClassType> getInterfaces();
	
	
	public default boolean hasField(FieldDescriptor descriptor) {
		return hasFieldByDescriptor(methodDescriptor -> methodDescriptor.equals(descriptor));
	}
	
	public default boolean hasMethod(MethodDescriptor descriptor) {
		return hasMethodByDescriptor(methodDescriptor -> methodDescriptor.equals(descriptor));
	}
	
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate);
	
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate);
}
