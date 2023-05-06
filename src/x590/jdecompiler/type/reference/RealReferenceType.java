package x590.jdecompiler.type.reference;

import java.util.List;

import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Описывает ссылочный тип, доступный в JVM - класс или массив,
 * т.е. типы, имеющие экземпляр {@link java.lang.Class}.
 */
public abstract class RealReferenceType extends ReferenceType {
	
	private @Nullable Class<?> classInstance;
	private boolean triedLoadClass;
	
	private @Nullable ClassType superType;
	private @Nullable @Immutable List<? extends ClassType> interfaces;
	private boolean triedLoadSuperTypes;
	

	public RealReferenceType() {}
	
	public RealReferenceType(Class<?> classInstance) {
		this.classInstance = classInstance;
		this.triedLoadClass = true;
	}
	
	public RealReferenceType(ClassType superType, @Immutable List<? extends ClassType> interfaces) {
		this.superType = superType;
		this.interfaces = interfaces;
		this.triedLoadSuperTypes = true;
	}
	
	
	@Override
	public abstract String getBinaryName();
	
	public abstract String getClassEncodedName();
	
	
	public @Nullable Class<?> getClassInstance() {
		if(triedLoadClass) {
			return classInstance;
		}
		
		triedLoadClass = true;
		
		String name = getBinaryName();
		
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
	
	protected void setInterfaces(@Immutable List<? extends ClassType> interfaces) {
		this.interfaces = interfaces;
	}
	
	@Override
	public @Nullable ClassType getSuperType() {
		if(!triedLoadSuperTypes) {
			triedLoadSuperTypes = true;
			tryLoadSuperTypes();
		}
		
		return superType;
	}
	
	@Override
	public @Nullable @Immutable List<? extends ClassType> getInterfaces() {
		if(!triedLoadSuperTypes) {
			triedLoadSuperTypes = true;
			tryLoadSuperTypes();
		}
		
		return interfaces;
	}
	
	/** Метод, который пытается загрузить суперкласс и суперинтерфейсы. */
	protected abstract void tryLoadSuperTypes();
}
