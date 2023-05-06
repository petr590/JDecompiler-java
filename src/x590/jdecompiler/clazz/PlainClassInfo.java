package x590.jdecompiler.clazz;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import x590.jdecompiler.attribute.annotation.Annotation;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.RealReferenceType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class PlainClassInfo implements IClassInfo {
	
	private final ClassModifiers modifiers;
	private final RealReferenceType thisType;
	private final @Nullable ClassType superType;
	private final @Nullable @Immutable List<? extends ClassType> interfaces;
	private GenericParameters<GenericDeclarationType> signatureParameters = GenericParameters.empty();
	private final @Immutable List<? extends FieldInfo> fieldInfos;
	private final @Immutable List<? extends MethodInfo> methodInfos;
	private final @Immutable List<? extends Annotation> annotations;
	
	private PlainClassInfo(RealReferenceType thisType, Class<?> clazz, ConstantPool pool) {
		this.modifiers = ClassModifiers.of(clazz.getModifiers());
		this.thisType = thisType;
		this.superType = thisType.getSuperType();
		this.interfaces = thisType.getInterfaces();
		
		this.signatureParameters = GenericParameters.of(
				Arrays.stream(clazz.getTypeParameters())
					.map(parameter -> GenericDeclarationType.fromTypeVariable(parameter, this)).toList()
		);
		
		this.fieldInfos =
				Arrays.stream(clazz.getDeclaredFields())
					.map(field -> FieldInfo.fromReflectField(thisType, field, this)).toList();
		
		this.methodInfos = Stream.concat(
				Arrays.stream(clazz.getDeclaredMethods())
					.map(method -> MethodInfo.fromReflectMethod(thisType, method, this)),
				Arrays.stream(clazz.getDeclaredConstructors())
					.map(constructor -> MethodInfo.fromReflectConstructor(thisType, constructor, this))
			).toList();
		
		this.annotations = Arrays.stream(clazz.getDeclaredAnnotations())
				.map(annotation -> Annotation.fromReflectAnnotation(pool, annotation)).toList();
	}
	
	static @Nullable PlainClassInfo fromClassType(RealReferenceType thisType, ConstantPool pool) {
		Class<?> clazz = thisType.getClassInstance();
		return clazz != null ? new PlainClassInfo(thisType, clazz, pool) : null;
	}
	
	@Override
	public ClassModifiers getModifiers() {
		return modifiers;
	}
	
	@Override
	public RealReferenceType getThisType() {
		return thisType;
	}
	
	@Override
	public @Nullable ClassType getSuperType() {
		return superType;
	}
	
	@Override
	public @Nullable @Immutable List<? extends ClassType> getInterfaces() {
		return interfaces;
	}
	
	@Override
	public GenericParameters<GenericDeclarationType> getSignatureParameters() {
		return signatureParameters;
	}
	
	@Override
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate) {
		return fieldInfos.stream().anyMatch(fieldInfo -> predicate.test(fieldInfo.getDescriptor()));
	}
	
	@Override
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate) {
		return methodInfos.stream().anyMatch(methodInfo -> predicate.test(methodInfo.getDescriptor()));
	}
	
	@Override
	public Optional<? extends FieldInfo> findFieldInfo(FieldDescriptor descriptor) {
		return fieldInfos.stream().filter(fieldInfo -> fieldInfo.getDescriptor().equals(descriptor)).findAny();
	}
	
	@Override
	public Optional<? extends MethodInfo> findMethodInfo(MethodDescriptor descriptor) {
		return methodInfos.stream().filter(methodInfo -> methodInfo.getDescriptor().equals(descriptor)).findAny();
	}
	
	
	@Override
	public Optional<? extends Annotation> findAnnotation(ClassType type) {
		return annotations.stream().filter(annotation -> annotation.getType().equals(type)).findAny();
	}
}
