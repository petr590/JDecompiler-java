package x590.jdecompiler.type;

import java.util.function.Function;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.IllegalTypeException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.IArrayType;
import x590.jdecompiler.type.reference.ReferenceType;
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
		this.encodedName = "UncertainClassType:" + widestType.getEncodedName() +
				(narrowestType == null ? "" : ":" + narrowestType.getEncodedName());
	}
	
	private UncertainReferenceType(ReferenceType widestType) {
		this(widestType, null);
	}
	
	
	public static Type getInstance(ReferenceType widestType) {
		return new UncertainReferenceType(widestType);
	}
	
	public static Type getInstance(ReferenceType widestType, @Nullable ReferenceType narrowestType, CastingKind kind) {
		if(narrowestType != null) {
			if(widestType.equalsIgnoreSignature(narrowestType))
				return narrowestType;
			
			if(widestType.isDefinitelySubclassOf(narrowestType))
				return null;
			
			if(widestType instanceof ArrayType widestArray && narrowestType instanceof ArrayType narrowestArray) {
				
				Type widestMember, narrowestMember;
				int nestingLevel = widestArray.getNestingLevel();
				
				if(nestingLevel == narrowestArray.getNestingLevel()) {
					widestMember    = widestArray.getMemberType();
					narrowestMember = narrowestArray.getMemberType();
				} else {
					widestMember    = widestArray.getElementType();
					narrowestMember = narrowestArray.getElementType();
					nestingLevel = 1;
				}
				
				if(widestMember.isReferenceType() && narrowestMember.isReferenceType())
					return ArrayType.forType(getInstance((ReferenceType)widestMember, (ReferenceType)narrowestMember, kind), nestingLevel);
				
				return ArrayType.forNullableType(widestMember.castNoexcept(narrowestMember, kind), nestingLevel);
			}
		}
		
		return new UncertainReferenceType(widestType, narrowestType);
	}
	
	
	public @Nullable ReferenceType getNarrowestType() {
		return narrowestType;
	}
	
	public ReferenceType getWidestType() {
		return widestType;
	}
	
	
	private ReferenceType getNonNullType() {
		return narrowestType == null ? widestType : narrowestType;
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printObject(getNonNullType(), classinfo);
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
		return getNonNullType().getName();
	}
	
	@Override
	public String getNameForVariable() {
		return getNonNullType().getNameForVariable();
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
	protected boolean canCastToNarrowest(Type other) {
		return castImpl(other, CastingKind.NARROWEST) != null;
	}
	
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		if(this.equals(other))
			return this;
		
		if(other instanceof ReferenceType referenceType) {
			
			if(referenceType.isDefinitelySubclassOf(widestType) && (narrowestType == null || narrowestType.isDefinitelySubclassOf(referenceType))) {
				return kind.isNarrowest() ?
						getInstance(referenceType, narrowestType, kind) :
						getInstance(widestType, referenceType, kind);
			}
			
			if(kind.isNarrowest() ?
				widestType.isDefinitelySubclassOf(referenceType) :
				referenceType.isDefinitelySubclassOf(narrowestType)) {
				
				return this;
			}
			
			return null;
		}
		
		if(other instanceof UncertainReferenceType uncertainType) {
			if(kind.isNarrowest()) {
				ReferenceType widestType = chooseNarrowestFrom(this.widestType, uncertainType.widestType);
				
				if(widestType != null) {
					return getInstance(widestType, this.narrowestType, kind);
				}
				
			} else {
				ReferenceType narrowestType = chooseWidestFrom(this.narrowestType, uncertainType.narrowestType);
				
				if(narrowestType != null) {
					return getInstance(this.widestType, narrowestType, kind);
				}
			}
		}
		
		return null;
	}
	
	private static @Nullable ReferenceType chooseNarrowestFrom(ReferenceType type1, ReferenceType type2) {
		return  type1.isDefinitelySubclassOf(type2) ? type1 :
				type2.isDefinitelySubclassOf(type1) ? type2 : null;
	}
	
	private static @Nullable ReferenceType chooseWidestFrom(ReferenceType type1, ReferenceType type2) {
		return  type1.isDefinitelySubclassOf(type2) ? type2 :
				type2.isDefinitelySubclassOf(type1) ? type1 : null;
	}
	
	private static @Nullable ReferenceType chooseNarrowestFromNullable(@Nullable ReferenceType type1, @Nullable ReferenceType type2) {
		return  type1 == null ? type2 :
				type2 == null ? type1 :
				chooseNarrowestFrom(type1, type2);
	}
	
	@Override
	protected Type reversedCastImpl(Type other, CastingKind kind) {
		if(this.equals(other))
			return this;
		
		if(other instanceof ReferenceType referenceType) {
			
			if(kind.isNarrowest() ?
				referenceType.isDefinitelySubclassOf(widestType) :
				widestType.isDefinitelySubclassOf(referenceType) || narrowestType != null && referenceType.isDefinitelySubclassOf(narrowestType)) {
				
				return referenceType;
			}
		}
		
		return null;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		getNonNullType().addImports(classinfo);
	}
	
	@Override
	public BasicType reduced() {
		return getNonNullType().reduced();
	}
}
