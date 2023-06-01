package x590.jdecompiler.type.primitive;

import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.BasicType;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.WrapperClassType;
import x590.util.annotation.Nullable;

import static x590.jdecompiler.type.UncertainIntegralType.INCLUDE_BOOLEAN;
import static x590.jdecompiler.type.UncertainIntegralType.INCLUDE_CHAR;

import x590.jdecompiler.clazz.ClassInfo;

public abstract class PrimitiveType extends BasicType {
	
	public static final PrimitiveType
			BYTE    = ByteType.INSTANCE,
			SHORT   = ShortType.INSTANCE,
			CHAR    = CharType.INSTANCE,
			INT     = IntType.INSTANCE,
			LONG    = LongType.INSTANCE,
			FLOAT   = FloatType.INSTANCE,
			DOUBLE  = DoubleType.INSTANCE,
			BOOLEAN = BooleanType.INSTANCE,
			VOID    = VoidType.INSTANCE;
	
	
	public static final int
			BYTE_CAPACITY  = 1,
			SHORT_CAPACITY = 2,
			CHAR_CAPACITY  = 2,
			INT_CAPACITY   = 4;
	
	
	public static final Type
			BYTE_SHORT_INT_CHAR_BOOLEAN = UncertainIntegralType.getInstance(BYTE_CAPACITY,  INT_CAPACITY, INCLUDE_CHAR | INCLUDE_BOOLEAN),
			BYTE_SHORT_INT_CHAR         = UncertainIntegralType.getInstance(BYTE_CAPACITY,  INT_CAPACITY, INCLUDE_CHAR),
			BYTE_SHORT_INT              = UncertainIntegralType.getInstance(BYTE_CAPACITY,  INT_CAPACITY),
			SHORT_INT_CHAR              = UncertainIntegralType.getInstance(SHORT_CAPACITY, INT_CAPACITY, INCLUDE_CHAR),
			INT_CHAR                    = UncertainIntegralType.getInstance(INT_CAPACITY,   INT_CAPACITY, INCLUDE_CHAR),
			SHORT_INT                   = UncertainIntegralType.getInstance(SHORT_CAPACITY, INT_CAPACITY),
			BYTE_BOOLEAN                = UncertainIntegralType.getInstance(BYTE_CAPACITY,  BYTE_CAPACITY, INCLUDE_BOOLEAN),
			INT_BOOLEAN                 = UncertainIntegralType.getInstance(INT_CAPACITY,   INT_CAPACITY,  INCLUDE_BOOLEAN);
	
	
	private final String encodedName, name, nameForVariable;
	
	public PrimitiveType(String encodedName, String name, String nameForVariable) {
		this.encodedName = encodedName;
		this.name = name;
		this.nameForVariable = nameForVariable;
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.write(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String getEncodedName() {
		return encodedName;
	}

	@Override
	public String getBinaryName() {
		return encodedName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getNameForVariable() {
		return nameForVariable;
	}
	
	@Override
	protected boolean canCastToNarrowestImpl(Type other) {
		return this == other;
	}
	
	@Override
	protected boolean canCastToWidestImpl(Type other) {
		return this == other;
	}
	
	@Override
	protected final Type castToNarrowestImpl(Type other) {
		return this.canCastToNarrowestImpl(other) ? this : null;
	}
	
	@Override
	protected final Type castToWidestImpl(Type other) {
		return this.canCastToWidestImpl(other) ? this : null;
	}
	
	@Override
	public @Nullable Type castToGeneralNoexcept(Type other, GeneralCastingKind kind) {
		if(other instanceof WrapperClassType wrapper) {
			return this.castToGeneralNoexcept(wrapper.getPrimitiveType(), kind);
		}
		
		if(kind == GeneralCastingKind.BINARY_OPERATOR) {
			if((this == CHAR || this.isIntegral()) && (other == CHAR || other.isIntegral())) {
				return INT;
			}
			
		} else {
			if(this == other) {
				return this;
			}
			
			if(this == CHAR && other.isIntegral() || other == CHAR && this.isIntegral()) {
				return INT;
			}
			
			if(this instanceof IntegralType integralType1 && other instanceof IntegralType integralType2) {
				return integralType1.getCapacity() > integralType2.getCapacity() ? this : other;
			}
		}
		
		return super.castToGeneralNoexcept(other, kind);
	}
	
	@Override
	public @Nullable Type implicitCastToGeneralNoexcept(Type other, GeneralCastingKind kind) {
		if(other instanceof WrapperClassType wrapper) {
			return this.implicitCastToGeneralNoexcept(wrapper.getPrimitiveType(), kind);
		}
		
		return super.implicitCastToGeneralNoexcept(other, kind);
	}
	
	public Type toUncertainIntegralType() {
		return this;
	}
	
	public abstract ClassType getWrapperType();
}
