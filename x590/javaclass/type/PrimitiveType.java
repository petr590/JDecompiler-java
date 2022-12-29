package x590.javaclass.type;

import x590.javaclass.ClassInfo;

import static x590.javaclass.type.UncertainIntegralType.INCLUDE_BOOLEAN;
import static x590.javaclass.type.UncertainIntegralType.INCLUDE_CHAR;

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
	public String toString(ClassInfo classinfo) {
		return name;
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
	public final boolean isPrimitive() {
		return true;
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
	
	public Type toVariableCapacityIntegralType() {
		return this;
	}
	
	public abstract ClassType getWrapperType();
}