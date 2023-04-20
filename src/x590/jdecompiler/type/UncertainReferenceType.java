package x590.jdecompiler.type;

import java.util.function.Function;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.IllegalTypeException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.util.annotation.Nullable;

/**
 * Когда ссылочный тип неизвестен точно
 */
public final class UncertainReferenceType extends Type implements IArrayType {
	
	/** Наиболее широкий тип */
	private final ReferenceType widestType;
	
	/** Наиболее узкий тип. Если он равен {@literal null}, то {@literal this}
	 * обозначает тип {@link #widestType} и любой его подтип */
	private final @Nullable ReferenceType narrowestType;
	
	private final String encodedName;
	
	private UncertainReferenceType(ReferenceType widestType, @Nullable ReferenceType narrowestType) {
		this.widestType = widestType;
		this.narrowestType = narrowestType;
		this.encodedName = "UncertainClassType:" + widestType.getClassEncodedName() +
				(narrowestType == null ? "" : ":" + narrowestType.getClassEncodedName());
	}
	
	private UncertainReferenceType(ReferenceType widestType) {
		this(widestType, null);
	}
	
	
	public static Type getInstance(ReferenceType widestType) {
		return new UncertainReferenceType(widestType);
	}
	
	public static Type getInstance(ReferenceType widestType, @Nullable ReferenceType narrowestType, boolean widest) {
		if(narrowestType != null) {
			if(widestType.equals(narrowestType))
				return widestType;
			
			if(widestType instanceof ArrayType widestArray && narrowestType instanceof ArrayType narrowestArray) {
				
				Type widestMember, narrowestMember;
				int nestingLevel = widestArray.getNestingLevel();
				
				if(widestArray.getNestingLevel() == narrowestArray.getNestingLevel()) {
					widestMember    = widestArray.getMemberType();
					narrowestMember = narrowestArray.getMemberType();
				} else {
					widestMember    = widestArray.getElementType();
					narrowestMember = narrowestArray.getElementType();
					nestingLevel = 1;
				}
				
				if(widestMember.isReferenceType() && narrowestMember.isReferenceType())
					return ArrayType.forType(getInstance((ReferenceType)widestMember, (ReferenceType)narrowestMember, widest), nestingLevel);
				
				return ArrayType.forNullableType(widest ?
						widestMember.castToWidestNoexcept(narrowestMember) :
						widestMember.castToNarrowestNoexcept(narrowestMember), nestingLevel);
			}
		}
		
		return new UncertainReferenceType(widestType, narrowestType);
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
		return narrowestType == null ?
				"(" + widestType.toString() + ")" :
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
	public Type getMemberType() {
		return getIArrayType(IArrayType::getMemberType).getMemberType();
	}
	
	@Override
	public Type getElementType() {
		return getIArrayType(IArrayType::getElementType).getElementType();
	}
	
	private IArrayType getIArrayType(Function<IArrayType, Type> getter) {
		if(narrowestType == null) {
			if(widestType instanceof IArrayType arrayWidestType) {
				return arrayWidestType;
			} else {
				throw new IllegalTypeException(this + " is not an array");
			}
		}
		
		if(narrowestType instanceof IArrayType arrayNarrowestType &&
			widestType instanceof IArrayType arrayWidestType) {
			
			Type narrowestElement = getter.apply(arrayNarrowestType);
			Type widestElement = getter.apply(arrayWidestType);
			
			if(narrowestElement instanceof ReferenceType narrowestReferenceElement &&
				widestElement instanceof ReferenceType widestReferenceElement) {
			
				return new UncertainReferenceType(widestReferenceElement, narrowestReferenceElement);
			} else {
				throw new IllegalTypeException(this + " is not reference type");
			}
			
		} else {
			throw new IllegalTypeException(this + " is not an array");
		}
	}
	
	
	@Override
	protected boolean canCastTo(Type other) {
		return castImpl(other, false) != null;
	}
	
	
	private Type castImpl(Type other, boolean widest) {
		if(this.equals(other))
			return this;
		
		if(other instanceof ReferenceType referenceType) {
			
			if(referenceType.isSubclassOf(widestType) && (narrowestType == null || narrowestType.isSubclassOf(referenceType))) {
				return widest ?
						getInstance(widestType, referenceType, widest) :
						getInstance(referenceType, narrowestType, widest);
			}
			
			if(widest ?
				referenceType.isSubclassOf(narrowestType) :
				widestType.isSubclassOf(referenceType)) {
				
				return this;
			}
			
			return null;
		}
		
		if(other instanceof UncertainReferenceType uncertainType) {
			if(!widest) {
				ReferenceType widestType = chooseNarrwestFrom(this.widestType, uncertainType.widestType);
				
				if(widestType != null) {
					return getInstance(widestType, chooseWidestFrom(this.narrowestType, uncertainType.widestType), widest);
				}
			}
		}
		
		return null;
	}
	
	private static @Nullable ReferenceType chooseNarrwestFrom(ReferenceType type1, ReferenceType type2) {
		return  type1.isSubclassOf(type2) ? type1 :
				type2.isSubclassOf(type1) ? type2 : null;
	}
	
	private static @Nullable ReferenceType chooseWidestFrom(@Nullable ReferenceType type1, @Nullable ReferenceType type2) {
		return  type1 == null ? type2 :
				type2 == null ? type1 :
				type1.isSubclassOf(type2) ? type1 :
				type2.isSubclassOf(type1) ? type2 : null;
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
