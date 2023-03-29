package x590.jdecompiler.type;

import java.util.List;

import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public abstract class ReferenceType extends BasicType {
	
	private @Nullable Class<?> classInstance;
	private boolean triedLoadClass;
	
	private @Nullable ClassType superType;
	private @Nullable @Immutable List<ClassType> interfaces;
	private boolean triedLoadSuperTypes;
	
	
	public ReferenceType() {}
	
	public ReferenceType(Class<?> classInstance) {
		this.classInstance = classInstance;
		this.triedLoadClass = true;
	}
	
	public ReferenceType(ClassType superType, @Immutable List<ClassType> interfaces) {
		this.superType = superType;
		this.interfaces = interfaces;
		this.triedLoadSuperTypes = true;
	}
	
	@Override
	public final boolean isAnyReferenceType() {
		return true;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	public String getClassEncodedName() {
		return getEncodedName();
	}
	
	
	public @Nullable Class<?> getClassInstance() {
		if(triedLoadClass) {
			return classInstance;
		}
		
		triedLoadClass = true;
		
		String name = getClassEncodedName().replace('/', '.');
		
		try {
			classInstance = Class.forName(name);
		} catch(ClassNotFoundException ex) {
			Logger.warningFormatted("Class \"%s\" not found among java classes", name);
		}
		
		return classInstance;
	}
	
	
	protected void setClassInstance(Class<?> classInstance) {
		this.classInstance = classInstance;
		this.triedLoadClass = true;
	}
	
	protected void setSuperType(ClassType superType) {
		this.superType = superType;
	}
	
	protected void setInterfaces(List<ClassType> interfaces) {
		this.interfaces = interfaces;
	}
	
	public @Nullable ClassType getSuperType() {
		if(triedLoadSuperTypes)
			return superType;
		
		triedLoadSuperTypes = true;
		tryLoadSuperTypes();
		return superType;
	}
	
	public @Nullable @Immutable List<ClassType> getInterfaces() {
		if(triedLoadSuperTypes)
			return interfaces;
		
		triedLoadSuperTypes = true;
		tryLoadSuperTypes();
		return interfaces;
	}
	
	public boolean isSubclassOf(ReferenceType other) {
		if(other == ClassType.OBJECT || this.equals(other))
			return true;
		
		var superType = getSuperType();
		
		return superType != null && superType.isSubclassOf(other) ||
				interfaces != null && interfaces.stream().anyMatch(interfaceType -> interfaceType.isSubclassOf(other));
	}
	
	/** Метод, который пытается загрузить суперкласс и суперинтерфейсы.
	 *  */
	protected void tryLoadSuperTypes() {
		// По умолчанию ничего не делает
	}
	
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.canCastTo(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastTo(other) ? other : null;
	}
}
