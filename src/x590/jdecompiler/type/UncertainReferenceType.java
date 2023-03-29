package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.util.annotation.Nullable;

/**
 * Когда ссылочный тип неизвестен точно
 */
public final class UncertainReferenceType extends Type {
	
	/** Наиболее широкий тип */
	private final ReferenceType widestType;
	
	/** Наиболее узкий тип. Если он равен {@literal null}, то {@literal this}
	 * обозначает тип {@link #widestType} и любой его подтип */
	private final @Nullable ReferenceType narrowestType;
	
	private final String encodedName;
	
	public UncertainReferenceType(ReferenceType widestType, @Nullable ReferenceType narrowestType) {
		this.widestType = widestType;
		this.narrowestType = narrowestType;
		this.encodedName = "UncertainClassType:" + widestType.getClassEncodedName() +
				(narrowestType == null ? "" : ":" + narrowestType.getClassEncodedName());
	}
	
	public UncertainReferenceType(ReferenceType widestType) {
		this(widestType, null);
	}
	
	
	public Type getInstance(ReferenceType widestType, @Nullable ReferenceType narrowestType) {
		return narrowestType != null && widestType.equals(narrowestType) ? widestType :
			new UncertainReferenceType(widestType, narrowestType);
	}
	
	
	private ReferenceType getNotNullType() {
		return narrowestType == null ? widestType : narrowestType;
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printlnObject(getNotNullType(), classinfo);
	}
	
	@Override
	public String toString() {
		return narrowestType == null ? "(" + widestType.toString() + ")" :
			"(" + widestType.toString() + " - " + narrowestType.toString() + ")";
	}
	
	@Override
	public String getEncodedName() {
		return encodedName;
	}
	
	@Override
	public String getName() {
		return getNotNullType().getName();
	}
	
	@Override
	public String getNameForVariable() {
		return getNotNullType().getNameForVariable();
	}
	
	
	@Override
	public final boolean isAnyReferenceType() {
		return true;
	}
	
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return false;
	}
	
	
	private Type castImpl(Type other, boolean widest) {
		if(this.equals(other))
			return this;
		
		if(other instanceof ReferenceType referenceType) {
			
			if(referenceType.isSubclassOf(widestType) && (narrowestType == null || narrowestType.isSubclassOf(referenceType))) {
				return widest ?
						getInstance(widestType, referenceType) :
						getInstance(referenceType, narrowestType);
			}
			
			if(widest ?
				referenceType.isSubclassOf(narrowestType) :
				widestType.isSubclassOf(referenceType)) {
				
				return this;
			}
		}
		
		return null;
	}
	
	
	private Type reversedCastImpl(Type other, boolean widest) {
		if(this.equals(other))
			return this;
		
		if(other instanceof ReferenceType referenceType) {
			
			if(widest ?
				widestType.isSubclassOf(referenceType) || narrowestType != null && referenceType.isSubclassOf(narrowestType) :
				referenceType.isSubclassOf(widestType)) {
				
				return referenceType;
			}
		}
		
		return null;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		getNotNullType().addImports(classinfo);
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return castImpl(other, false);
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return castImpl(other, true);
	}
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return reversedCastImpl(other, false);
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return reversedCastImpl(other, true);
	}
	
	@Override
	public Type reduced() {
		return narrowestType != null ? narrowestType : widestType;
	}
}
