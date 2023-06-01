package x590.jdecompiler.type.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import x590.util.Logger;
import x590.util.Pair;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Описывает ссылочный тип, доступный в JVM - класс или массив,
 * т.е. типы, имеющие экземпляр {@link java.lang.Class}.
 */
public abstract class RealReferenceType extends ReferenceType {
	
	private @Nullable Class<?> classInstance;
	private boolean triedLoadClass;
	
	private @Nullable ClassType genericSuperType;
	private @Nullable @Immutable List<? extends ClassType> interfaces, genericInterfaces;
	private boolean triedLoadSuperTypes;
	
	private @Nullable @Immutable List<BiConsumer<? super ClassType, ? super List<? extends ClassType>>> initCallbacks;
	

	public RealReferenceType() {}
	
	public RealReferenceType(Class<?> classInstance) {
		this.classInstance = classInstance;
		this.triedLoadClass = true;
	}
	
	public RealReferenceType(ClassType superType, @Immutable List<? extends ClassType> genericInterfaces) {
		this.genericSuperType = superType;
		this.genericInterfaces = genericInterfaces;
		this.interfaces = interfacesFromGenericInterfaces(genericInterfaces);
		this.triedLoadSuperTypes = true;
	}
	
	private List<? extends ClassType> interfacesFromGenericInterfaces(@Nullable List<? extends ClassType> genericInterfaces) {
		if(genericInterfaces == null)
			return null;
		
		var interfaces = genericInterfaces.stream().map(ClassType::getRawType).toList();
		return interfaces.equals(genericInterfaces) ? genericInterfaces : interfaces;
	}
	
	public abstract String getClassEncodedName();

	@Override
	public abstract @Nullable String getBinaryName();
	
	
	public @Nullable Class<?> getClassInstance() {
		if(triedLoadClass) {
			return classInstance;
		}
		
		triedLoadClass = true;
		
		String name = getBinaryName();

		if(name != null) {
			try {
				classInstance = Class.forName(name);
			} catch (ClassNotFoundException ex) {
				Logger.warningFormatted("Class \"%s\" not found among java classes", name);
			}
		}
		
		return classInstance;
	}
	
	
	protected void setClassInstance(Class<?> classInstance) {
		this.classInstance = classInstance;
		this.triedLoadClass = true;
	}
	
	@Override
	public @Nullable ClassType getSuperType() {
		tryLoadSuperTypesIfNotLoadedYet();
		var genericSuperType = this.genericSuperType;
		return genericSuperType == null ? null : genericSuperType.getRawType();
	}
	
	@Override
	public @Nullable ClassType getGenericSuperType() {
		tryLoadSuperTypesIfNotLoadedYet();
		return genericSuperType;
	}
	
	@Override
	public @Nullable @Immutable List<? extends ClassType> getInterfaces() {
		tryLoadSuperTypesIfNotLoadedYet();
		return interfaces;
	}
	
	@Override
	public @Nullable @Immutable List<? extends ClassType> getGenericInterfaces() {
		tryLoadSuperTypesIfNotLoadedYet();
		return genericInterfaces;
	}

	private void tryLoadSuperTypesIfNotLoadedYet() {
		if(!triedLoadSuperTypes) {
			triedLoadSuperTypes = true;
			var types = tryLoadSuperTypes();
			var genericSuperType = this.genericSuperType = types.first();
			this.genericInterfaces = types.second();
			this.interfaces = interfacesFromGenericInterfaces(genericInterfaces);
			
			var initCallbacks = this.initCallbacks;
			
			var superType = genericSuperType == null ? null : genericSuperType.getRawType();
			var interfaces = this.interfaces;
			
			if(initCallbacks != null) {
				initCallbacks.forEach(callback -> callback.accept(superType, interfaces));
				initCallbacks.clear();
			}
		}
	}
	
	/** Метод, который пытается загрузить суперкласс и суперинтерфейсы (с дженериками). */
	protected abstract Pair<ClassType, List<ClassType>> tryLoadSuperTypes();
	
	
	/** Добавляет callback, который вызывается после инициализации */
	public void afterInit(BiConsumer<? super ClassType, ? super List<? extends ClassType>> callback) {
		if(initCallbacks == null)
			initCallbacks = new ArrayList<>();
		
		initCallbacks.add(callback);
	}
}
