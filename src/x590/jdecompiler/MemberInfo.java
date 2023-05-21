package x590.jdecompiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.modifiers.ClassEntryModifiers;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainReferenceType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.util.annotation.Nullable;

/**
 * Представляет общий шаблон для {@link FieldInfo} и {@link MethodInfo}
 */
public abstract class MemberInfo<D extends Descriptor<D>, M extends ClassEntryModifiers> {
	
	private final D descriptor, genericDescriptor;
	private final M modifiers;
	private @Nullable Int2ObjectMap<String> enumTable;
	
	public MemberInfo(D descriptor, D genericDescriptor, M modifiers) {
		this.descriptor = descriptor;
		this.genericDescriptor = genericDescriptor;
		this.modifiers = modifiers;
	}
	
	
	public D getDescriptor() {
		return descriptor;
	}
	
	public D getGenericDescriptor() {
		return genericDescriptor;
	}
	
	/** @return Конкретный generic дескриптор для переданного объекта,
	 * или generic дескриптор по умолчанию, если объект равен {@literal null}. */
	public D getGenericDescriptor(@Nullable Operation object, ConstantPool pool) {
		if(object == null)
			return genericDescriptor;
		
		Type type = object.getReturnType();
		
		if(type instanceof ReferenceType referenceType) {
			return getDefiniteGenericDescriptor(referenceType, pool);
			
		} else if(type instanceof UncertainReferenceType uncertainReferenceType) {
			return getDefiniteGenericDescriptor(uncertainReferenceType.getNarrowestType(), pool);
			
		} else {
			return genericDescriptor;
		}
	}
	
	
	private D getDefiniteGenericDescriptor(ReferenceType operationType, ConstantPool pool) {
		
		// Список типов в порядке от самого широкого к самому узкому
		List<ReferenceType> supertypes = new ArrayList<>();
		
		if(findSuperClasses(operationType, descriptor.getDeclaringClass(), supertypes)) {
			
			var foundClassinfo = ClassInfo.findIClassInfo(genericDescriptor.getDeclaringClass(), pool);
			
			if(foundClassinfo.isPresent()) {
				GenericParameters<GenericDeclarationType> srcParameters = foundClassinfo.get().getSignatureParameters();
				GenericParameters<? extends ReferenceType> parameters = srcParameters;
				
				for(ReferenceType currType : supertypes) {
					parameters = ReferenceType.narrowGenericParameters(currType, parameters, pool);
				}
				
				final int size = parameters.size();
				
				assert size == srcParameters.size();
				
				Map<GenericDeclarationType, ReferenceType> replaceTable = new HashMap<>(size);
				
				for(int i = 0; i < size; i++) {
					replaceTable.put(srcParameters.get(i), parameters.get(i));
				}
				
				return genericDescriptor.replaceAllTypes(replaceTable);
			}
		}
		
		return genericDescriptor;
	}
	
	private boolean findSuperClasses(@Nullable ReferenceType currentType, ReferenceType targetType,
			List<ReferenceType> superclasses) {
		
		if(currentType == null) {
			return false;
		}
		
		if(currentType.equalsIgnoreSignature(targetType)) {
			superclasses.add(currentType);
			return true;
		}
		
		if(findSuperClasses(currentType.getGenericSuperType(), targetType, superclasses)) {
			superclasses.add(currentType);
			return true;
		}
		
		var interfaces = currentType.getGenericInterfaces();
		
		if(interfaces != null) {
			for(ReferenceType interfaceType : interfaces) {
				if(findSuperClasses(interfaceType, targetType, superclasses)) {
					superclasses.add(currentType);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public M getModifiers() {
		return modifiers;
	}
	
	/**
	 * @return Таблицу enum значений, необходимых для правильной работы {@literal switch},
	 * или {@literal null}, если член класса не содержит таблицу.
	 */
	public @Nullable Int2ObjectMap<String> getEnumTable() {
		return enumTable;
	}
	
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {
		this.enumTable = enumTable;
	}
	
	
	@Override
	public abstract String toString();
	
	
	@Override
	public int hashCode() {
		return Objects.hash(descriptor, modifiers);
	}
	
	@Override
	public abstract boolean equals(Object obj);
	
	public boolean equals(MemberInfo<D, M> other) {
		return this == other ||
				descriptor.equals(other.descriptor) &&
				modifiers.equals(other.modifiers);
	}
}
