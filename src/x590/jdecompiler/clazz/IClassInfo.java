package x590.jdecompiler.clazz;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import x590.jdecompiler.Descriptor;
import x590.jdecompiler.MemberInfo;
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
import x590.jdecompiler.type.reference.generic.NamedGenericType;
import x590.util.annotation.Immutable;

/**
 * Интерфейс ClassInfo. Позволяет создавать экземпляры IClassInfo
 * не только из JavaClass, но и из обычного объекта Class
 */
public interface IClassInfo {
	
	
	ClassModifiers getModifiers();
	
	RealReferenceType getThisType();
	
	Optional<ClassType> getOptionalSuperType();
	
	Optional<@Immutable List<? extends ClassType>> getOptionalInterfaces();
	
	default @Immutable List<? extends ClassType> getInterfacesOrEmpty() {
		return getOptionalInterfaces().orElseGet(Collections::emptyList);
	}
	
	GenericParameters<GenericDeclarationType> getSignatureParameters();
	
	
	default boolean isRecord() {
		var optionalSuperType = getOptionalSuperType();
		return optionalSuperType.isPresent() && optionalSuperType.get().equalsIgnoreSignature(ClassType.RECORD);
	}
	
	
	default boolean hasField(FieldDescriptor descriptor) {
		return hasFieldByDescriptor(descriptor::equals);
	}
	
	default boolean hasMethod(MethodDescriptor descriptor) {
		return hasMethodByDescriptor(descriptor::equals);
	}
	
	boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate);
	
	boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate);
	
	
	/** @return Optional с найденным FieldInfo или {@code Optional.empty()}, если FieldInfo не найден
	 * @apiNote Сравнивать дескрипторы нужно через {@link FieldDescriptor#equalsIgnoreClass(FieldDescriptor)}
	 * для корректной работы метода {@link #findFieldInfoInThisAndSuperClasses(FieldDescriptor)} */
	Optional<? extends FieldInfo> findFieldInfo(FieldDescriptor descriptor);
	
	/** @return Optional с найденным MethodInfo или {@code Optional.empty()}, если MethodInfo не найден
	 * @apiNote Сравнивать дескрипторы нужно через {@link MethodDescriptor#equalsIgnoreClass(MethodDescriptor)}
	 * для корректной работы метода {@link #findMethodInfoInThisAndSuperClasses(MethodDescriptor)} */
	Optional<? extends MethodInfo> findMethodInfo(MethodDescriptor descriptor);
	
	
	default Optional<? extends FieldInfo> findFieldInfoInThisAndSuperClasses(FieldDescriptor descriptor) {
		return findMemberInfoInThisAndSuperClasses(descriptor, IClassInfo::findFieldInfo);
	}
	
	default Optional<? extends MethodInfo> findMethodInfoInThisAndSuperClasses(MethodDescriptor descriptor) {
		return findMemberInfoInThisAndSuperClasses(descriptor, IClassInfo::findMethodInfo);
	}
	
	
	private <D extends Descriptor<D>, M extends MemberInfo<D, ?>> Optional<M>
			findMemberInfoInThisAndSuperClasses(D descriptor, BiFunction<IClassInfo, D, Optional<M>> finder) {
		
		var foundMethodInfo = finder.apply(this, descriptor);
		
		Function<ClassType, Optional<M>> typeFinder = type -> findInType(type, descriptor, finder);
		
		return foundMethodInfo.isPresent() ?
				foundMethodInfo :
				getOptionalSuperType().map(typeFinder)
					.orElseGet(() ->
							getInterfacesOrEmpty().stream()
									.map(typeFinder).findAny().orElse(null)
					);
	}

	private <D extends Descriptor<D>, M extends MemberInfo<D, ?>> Optional<M>
			findInType(RealReferenceType type, D descriptor, BiFunction<IClassInfo, D, Optional<M>> finder) {
		
		return ClassInfo.findIClassInfo(type)
				.map(classinfo -> classinfo.findMemberInfoInThisAndSuperClasses(descriptor, finder))
				.flatMap(Function.identity());
	}
	
	
	Optional<? extends Annotation> findAnnotation(ClassType type);
	
	
	default Optional<? extends GenericDeclarationType> findGenericType(String name) {
		return getSignatureParameters().findGenericType(name);
	}
	
	default ReferenceType findOrCreateGenericType(String name) {
		return findGenericType(name).map(type -> type.replaceUndefiniteGenericsToDefinite(this, GenericParameters.empty()))
				.orElseGet(() -> NamedGenericType.of(name));
	}
}
