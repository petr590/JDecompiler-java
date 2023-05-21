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
import x590.jdecompiler.type.reference.generic.SignatureParameterType;
import x590.util.annotation.Immutable;

/**
 * Интерфейс ClassInfo. Позволяет создавать экземпляры IClassInfo
 * не только из JavaClass, но и из обычного объекта Class
 */
public interface IClassInfo {
	
	
	public ClassModifiers getModifiers();
	
	public RealReferenceType getThisType();
	
	public Optional<ClassType> getOptionalSuperType();
	
	public Optional<@Immutable List<? extends ClassType>> getOptionalInterfaces();
	
	public default @Immutable List<? extends ClassType> getInterfacesOrEmpty() {
		return getOptionalInterfaces().orElseGet(Collections::emptyList);
	}
	
	public GenericParameters<GenericDeclarationType> getSignatureParameters();
	
	
	public default boolean isRecord() {
		var optionalSuperType = getOptionalSuperType();
		return optionalSuperType.isPresent() && optionalSuperType.get().equalsIgnoreSignature(ClassType.RECORD);
	}
	
	
	public default boolean hasField(FieldDescriptor descriptor) {
		return hasFieldByDescriptor(descriptor::equals);
	}
	
	public default boolean hasMethod(MethodDescriptor descriptor) {
		return hasMethodByDescriptor(descriptor::equals);
	}
	
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate);
	
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate);
	
	
	/** @return Optional с найденным FieldInfo или {@code Optional.empty()}, если FieldInfo не найден
	 * @apiNote Сравнивать дескрипторы нужно через {@link MethodDescriptor#equalsIgnoreClass(MethodDescriptor)}
	 * для корректной работы метода {@link #findMethodInfoInThisAndSuperClasses(MethodDescriptor)} */
	public Optional<? extends FieldInfo> findFieldInfo(FieldDescriptor descriptor);
	
	/** @return Optional с найденным MethodInfo или {@code Optional.empty()}, если MethodInfo не найден
	 * @apiNote Сравнивать дескрипторы нужно через {@link MethodDescriptor#equalsIgnoreClass(MethodDescriptor)}
	 * для корректной работы метода {@link #findMethodInfoInThisAndSuperClasses(FieldDescriptor)} */
	public Optional<? extends MethodInfo> findMethodInfo(MethodDescriptor descriptor);
	
	
	public default Optional<? extends FieldInfo> findFieldInfoInThisAndSuperClasses(FieldDescriptor descriptor) {
		return findMemberInfoInThisAndSuperClasses(descriptor, IClassInfo::findFieldInfo);
	}
	
	public default Optional<? extends MethodInfo> findMethodInfoInThisAndSuperClasses(MethodDescriptor descriptor) {
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
	
	
	public Optional<? extends Annotation> findAnnotation(ClassType type);
	
	
	public default Optional<? extends GenericDeclarationType> findGenericType(String name) {
		return getSignatureParameters().findGenericType(name);
	}
	
	public default ReferenceType findOrCreateGenericType(String name) {
		return findGenericType(name).map(type -> type.replaceUndefiniteGenericsToDefinite(this, GenericParameters.empty()))
				.orElseGet(() -> SignatureParameterType.of(name));
	}
}
