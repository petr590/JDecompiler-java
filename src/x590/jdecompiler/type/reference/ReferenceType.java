package x590.jdecompiler.type.reference;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.type.BasicType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Описывает любой ссылочный тип - класс, массив, дженерик
 */
public abstract class ReferenceType extends BasicType {
	
	@Override
	public final boolean isAnyReferenceType() {
		return true;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	
	/** Проверяет, что заданные модификаторы точно установлены.
	 * @return {@literal true}, если это так, {@literal false} в противном случае
	 * или если модификаторы получить невозможно */
	public boolean isDefinitely(int modifiers) {
		return false;
	}
	
	/** Проверяет, что заданные модификаторы точно не установлены.
	 * @return {@literal true}, если это так, {@literal false} в противном случае
	 * или если модификаторы получить невозможно */
	public boolean isDefinitelyNot(int modifiers) {
		return false;
	}
	
	/** @return Супертип текущего типа или {@literal null}, если супертип неизвестен */
	public abstract @Nullable ReferenceType getSuperType();
	
	/** @return Супертип текущего типа с generic параметрами или {@literal null}, если супертип неизвестен */
	public @Nullable ReferenceType getGenericSuperType() {
		return getSuperType();
	}
	
	/** @return Неизменяемый список интерфейсов, которые реализует текущий тип
	 * или {@literal null}, если интерфейсы неизвестны */
	public abstract @Nullable @Immutable List<? extends ReferenceType> getInterfaces();
	
	/** @return Неизменяемый список интерфейсов с generic параметрами, которые реализует текущий тип
	 * или {@literal null}, если интерфейсы неизвестны */
	public @Nullable @Immutable List<? extends ReferenceType> getGenericInterfaces() {
		return getInterfaces();
	}
	
	
	@Override
	public final boolean isDefinitelySubtypeOf(Type other) {
		return super.isDefinitelySubtypeOf(other) ||
				other instanceof ReferenceType referenceType &&
				isDefinitelySubclassOf(referenceType);
	}
	
	
	public boolean isDefinitelySubclassOf(ReferenceType other) {
		if(other == ClassType.OBJECT)
			return true;
		
		if(other.isDefinitely(ACC_FINAL))
			return this.equalsIgnoreSignature(other);
		
		return deepIsSubclassOf(other);
	}
	
	private boolean deepIsSubclassOf(ReferenceType other) {
		if(this.equalsIgnoreSignature(other))
			return true;
		
		var superType = getSuperType();
		var interfaces = getInterfaces();
		
		return  superType != null && superType.deepIsSubclassOf(other) ||
				interfaces != null && interfaces.stream().anyMatch(type -> ((ReferenceType)type).deepIsSubclassOf(other));
	}
	
	
	public boolean mayBeSubclassOf(ReferenceType other) {
		if(other == ClassType.OBJECT)
			return true;
		
		if(other.isDefinitely(ACC_FINAL))
			return this.equalsIgnoreSignature(other);
		
		return deepMayBeSubclassOf(other);
	}
	
	private boolean deepMayBeSubclassOf(ReferenceType other) {
		if(this.equalsIgnoreSignature(other))
			return true;
		
		var superType = getSuperType();
		var interfaces = getInterfaces();
		
		return  superType == null && interfaces == null ||
				superType != null && superType.deepMayBeSubclassOf(other) ||
				interfaces != null && interfaces.stream().anyMatch(type -> ((ReferenceType)type).deepMayBeSubclassOf(other));
	}
	
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.canCastToNarrowestImpl(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastToWidestImpl(other) ? this : null;
	}
	
	@Override
	protected abstract boolean canCastToNarrowestImpl(Type other);

	@Override
	protected abstract boolean canCastToWidestImpl(Type other);
	
	@Override
	public ReferenceType replaceUndefiniteGenericsToDefinite(IClassInfo classinfo, GenericParameters<GenericDeclarationType> parameters) {
		return this;
	}
	
	@Override
	public ReferenceType replaceAllTypes(@Immutable Map<GenericDeclarationType, ReferenceType> replaceTable) {
		return this;
	}
	
//	public abstract @Nullable GenericParameters<? extends ReferenceType> narrowGenericParameters(
//			ReferenceType prevType, GenericParameters<? extends ReferenceType> parameters);
	
	
	public static ReferenceType fromReflectType(java.lang.reflect.Type reflectType, IClassInfo classinfo) {
		if(reflectType instanceof Class<?> clazz) {
			return ClassType.fromClass(clazz);
		}
		
		if(reflectType instanceof ParameterizedType parameterizedType) {
			return ClassType.fromParameterizedType(parameterizedType, classinfo);
		}
		
		return classinfo.findOrCreateGenericType(reflectType.getTypeName());
	}
	
	
	public static @Nullable GenericParameters<? extends ReferenceType> narrowGenericParameters(
			ReferenceType type, GenericParameters<? extends ReferenceType> rootParameters) {
		
		if(type instanceof ClassType classType) {
			
			var foundClassinfo = ClassInfo.findIClassInfo(classType.getRawType());
			
			if(foundClassinfo.isPresent()) {
				var parameters = foundClassinfo.get().getSignatureParameters();
				var signature = classType.getSignature();
				
				if(parameters.size() == signature.size()) {
					final int size = parameters.size();
					
					Map<GenericDeclarationType, ReferenceType> replaceTable = new HashMap<>(size);
					
					for(int i = 0; i < size; i++) {
						replaceTable.put(parameters.get(i), signature.get(i));
					}
					
					return GenericParameters.replaceAllTypes(rootParameters, replaceTable);
					
				} else if(!parameters.isEmpty() && !signature.isEmpty()) {
					Logger.warning("Signature of " + classType + " is not matches with parameters " + parameters);
				}
			}
		}
		
		return rootParameters;
	}
}
