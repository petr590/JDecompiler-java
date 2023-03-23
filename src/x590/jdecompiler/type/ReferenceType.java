package x590.jdecompiler.type;

import java.util.List;

import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public abstract class ReferenceType extends BasicType {
	
	@Nullable ClassType superType;
	@Nullable @Immutable List<ClassType> interfaces;
	
	@Nullable Class<?> classInstance;
	boolean triedLoadClass, triedLoadSuperType;
	
	
	public @Nullable Class<?> getClassInstance() {
		if(triedLoadClass) {
			return classInstance;
		}
		
		triedLoadClass = true;
		
		try {
			classInstance = Class.forName(name);
		} catch(ClassNotFoundException ex) {
			Logger.warningFormatted("Class \"%s\" not found among java classes", name);
		}
		
		return classInstance;
	}
	
	
	public ReferenceType() {}
	
	public ReferenceType(ClassType superType) {
		this.superType = superType;
	}
	
	public ReferenceType(ClassType superType, List<ClassType> interfaces) {
		this.superType = superType;
		this.interfaces = interfaces;
	}
	
	public ReferenceType(String encodedName, String name, ClassType superType) {
		super(encodedName, name);
		this.superType = superType;
	}
	
	public ReferenceType(String encodedName, String name, ClassType superType, List<ClassType> interfaces) {
		super(encodedName, name);
		this.superType = superType;
		this.interfaces = interfaces;
	}
	
	@Override
	public final boolean isReferenceType() {
		return true;
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	public String getClassEncodedName() {
		return encodedName;
	}
	
	public @Nullable ClassType getSuperType() {
		if(triedLoadSuperType)
			return superType;
		
		triedLoadSuperType = true;
		tryLoadSuperType();
		return superType;
	}
	
	public @Nullable @Immutable List<ClassType> getInterfaces() {
		if(triedLoadSuperType)
			return interfaces;
		
		triedLoadSuperType = true;
		tryLoadSuperType();
		return interfaces;
	}
	
	public boolean isSubclassOf(ReferenceType other) {
		if(other == ClassType.OBJECT || this.equals(other))
			return true;
		
		var superType = getSuperType();
		
		return superType != null && superType.isSubclassOf(other) ||
				interfaces != null && interfaces.stream().anyMatch(interfaceType -> interfaceType.isSubclassOf(other));
	}
	
	protected void tryLoadSuperType() {
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
