package x590.jdecompiler.clazz;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import x590.jdecompiler.attribute.annotation.Annotation;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.RealReferenceType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.jdecompiler.type.reference.generic.SignatureParameterType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Интерфейс ClassInfo. Позволяет создавать экземпляры IClassInfo
 * не только из JavaClass, но и из обычного объекта Class
 */
public interface IClassInfo {
	
	public ClassModifiers getModifiers();
	
	public RealReferenceType getThisType();
	
	public @Nullable ClassType getSuperType();
	
	public @Nullable @Immutable List<? extends ClassType> getInterfaces();
	
	public GenericParameters<GenericDeclarationType> getSignatureParameters();
	
	
	public default boolean isRecord() {
		ClassType superType = getSuperType();
		return superType != null && superType.equals(ClassType.RECORD);
	}
	
	
	public default boolean hasField(FieldDescriptor descriptor) {
		return hasFieldByDescriptor(descriptor::equals);
	}
	
	public default boolean hasMethod(MethodDescriptor descriptor) {
		return hasMethodByDescriptor(descriptor::equals);
	}
	
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate);
	
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate);
	
	
	public Optional<? extends FieldInfo> findFieldInfo(FieldDescriptor descriptor);
	
	public Optional<? extends MethodInfo> findMethodInfo(MethodDescriptor descriptor);
	
	
	public Optional<? extends Annotation> findAnnotation(ClassType type);
	
	
	public default Optional<? extends GenericDeclarationType> findGenericType(String name) {
		return getSignatureParameters().findGenericType(name);
	}
	
	public default ReferenceType findOrCreateGenericType(String name) {
		return findGenericType(name).map(type -> type.toDefiniteGeneric(this, GenericParameters.empty()))
				.orElseGet(() -> SignatureParameterType.of(name));
	}
}
