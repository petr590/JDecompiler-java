package x590.jdecompiler.type;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.util.annotation.Nullable;

import static x590.jdecompiler.type.UncertainIntegralType.INCLUDE_BOOLEAN;
import static x590.jdecompiler.type.UncertainIntegralType.INCLUDE_CHAR;

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
	
	
	public static final UncertainIntegralType
			BYTE_SHORT_INT_CHAR_BOOLEAN = UncertainIntegralType.getInstance(1, 4, INCLUDE_BOOLEAN | INCLUDE_CHAR),
			BYTE_SHORT_INT_CHAR         = UncertainIntegralType.getInstance(1, 4, INCLUDE_CHAR),
			BYTE_SHORT_INT              = UncertainIntegralType.getInstance(1, 4),
			SHORT_INT_CHAR              = UncertainIntegralType.getInstance(2, 4, INCLUDE_CHAR),
			INT_CHAR                    = UncertainIntegralType.getInstance(4, 4, INCLUDE_CHAR),
			SHORT_INT                   = UncertainIntegralType.getInstance(2, 4),
			BYTE_BOOLEAN                = UncertainIntegralType.getInstance(1, 1, INCLUDE_BOOLEAN),
			INT_BOOLEAN                 = UncertainIntegralType.getInstance(4, 4, INCLUDE_BOOLEAN);
	
	
	public static final int CHAR_CAPACITY = 2;
	
	
	private final String nameForVariable;
	
	public PrimitiveType(String encodedName, String name, String nameForVariable) {
		super(encodedName, name);
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
	public String getNameForVariable() {
		return nameForVariable;
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return this == other;
	}
	
	protected boolean canCastToWidest(Type other) {
		return this == other;
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.canCastTo(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastToWidest(other) ? this : null;
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
