package x590.jdecompiler.type;

import java.util.List;

import x590.util.annotation.Nullable;

public abstract class ReferenceType extends BasicType {
	
	@Nullable ReferenceType superType;
	@Nullable List<ReferenceType> interfaces;
	
	public ReferenceType() {}
	
	public ReferenceType(ReferenceType superType) {
		this.superType = superType;
	}
	
	public ReferenceType(ReferenceType superType, List<ReferenceType> interfaces) {
		this.superType = superType;
		this.interfaces = interfaces;
	}
	
	public ReferenceType(String encodedName, String name, ReferenceType superType) {
		super(encodedName, name);
		this.superType = superType;
	}
	
	public ReferenceType(String encodedName, String name, ReferenceType superType, List<ReferenceType> interfaces) {
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
	
	
	public void assignSuperClasses(ReferenceType superType, List<ReferenceType> interfaces) {
		if(this.superType == null)
			this.superType = superType;
		
		if(this.interfaces == null)
			this.interfaces = interfaces;
	}
	
	public boolean isSubclassOf(ReferenceType other) {
		if(this.equals(other))
			return true;
		
		if(superType == null)
			tryLoadSuperType();
		
		return superType != null && superType.isSubclassOf(other) ||
				interfaces != null && interfaces.stream().anyMatch(interfaceType -> interfaceType.isSubclassOf(other));
	}
	
	protected void tryLoadSuperType() {}
	
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.canCastTo(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastTo(other) ? other : null;
	}
}
