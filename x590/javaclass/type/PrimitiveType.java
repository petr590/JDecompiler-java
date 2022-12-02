package x590.javaclass.type;

import x590.javaclass.ClassInfo;

public abstract class PrimitiveType extends BasicType {
	
	public static final PrimitiveType
			VOID    = VoidType.INSTANCE,
			BOOLEAN = BooleanType.INSTANCE,
			BYTE    = ByteType.INSTANCE,
			SHORT   = ShortType.INSTANCE,
			CHAR    = CharType.INSTANCE,
			INT     = IntType.INSTANCE,
			LONG    = LongType.INSTANCE,
			FLOAT   = FloatType.INSTANCE,
			DOUBLE  = DoubleType.INSTANCE;
	
	
	public static final VariableCapacityIntegralType
			  ANY_INT_OR_BOOLEAN = VariableCapacityIntegralType.getInstance(1, 4, VariableCapacityIntegralType.INCLUDE_BOOLEAN | VariableCapacityIntegralType.INCLUDE_CHAR),
			             ANY_INT = VariableCapacityIntegralType.getInstance(1, 4, VariableCapacityIntegralType.INCLUDE_CHAR),
			      ANY_SIGNED_INT = VariableCapacityIntegralType.getInstance(1, 4),
			CHAR_OR_SHORT_OR_INT = VariableCapacityIntegralType.getInstance(2, 4, VariableCapacityIntegralType.INCLUDE_CHAR),
			         CHAR_OR_INT = VariableCapacityIntegralType.getInstance(4, 4, VariableCapacityIntegralType.INCLUDE_CHAR),
			        SHORT_OR_INT = VariableCapacityIntegralType.getInstance(2, 4),
			     BYTE_OR_BOOLEAN = VariableCapacityIntegralType.getInstance(1, 1, VariableCapacityIntegralType.INCLUDE_BOOLEAN),
			      INT_OR_BOOLEAN = VariableCapacityIntegralType.getInstance(4, 4, VariableCapacityIntegralType.INCLUDE_BOOLEAN);
	
	
	public final String nameForVariable;
	
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
	protected boolean isSubtypeOfImpl(Type other) {
		return this == other;
	}
	
	@Override
	protected boolean canReverse(Type other) {
		return true;
	}
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.isSubtypeOf(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.isSubtypeOf(other) ? other : null;
	}
	
	public Type toVariableCapacityIntegralType() {
		return this;
	}
	
	public abstract ClassType getWrapperType();
}