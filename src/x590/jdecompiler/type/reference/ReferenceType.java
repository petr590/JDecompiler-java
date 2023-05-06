package x590.jdecompiler.type.reference;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.util.List;

import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.type.BasicType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
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
	
	
	public boolean isDefinitely(int modifiers) {
		return false;
	}
	
	public boolean isDefinitelyNot(int modifiers) {
		return false;
	}
	
	public abstract @Nullable ReferenceType getSuperType();
	
	public abstract @Nullable @Immutable List<? extends ReferenceType> getInterfaces();
	
	
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
		return this.canCastToNarrowest(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastToNarrowest(other) ? other : null;
	}
	
	@Override
	public ReferenceType toDefiniteGeneric(IClassInfo classinfo, GenericParameters<GenericDeclarationType> parameters) {
		return this;
	}
	
	
	public static ReferenceType fromReflectType(java.lang.reflect.Type reflectType, IClassInfo classinfo) {
		return reflectType instanceof Class<?> clazz ?
				ClassType.fromClass(clazz) :
				classinfo.findOrCreateGenericType(reflectType.getTypeName());
	}
}
