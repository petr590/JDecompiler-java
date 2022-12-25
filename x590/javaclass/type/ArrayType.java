package x590.javaclass.type;

import x590.javaclass.ClassInfo;
import x590.javaclass.exception.InvalidArrayNameException;
import x590.javaclass.io.ExtendedStringReader;

/**
 * Описывает тип массива, включая массив примитивов,
 * многомерные массивы и массивы SpecialType
 */
public final class ArrayType extends ReferenceType {
	
	public static final ArrayType
			ANY_ARRAY        = new ArrayType(AnyType.INSTANCE),
			ANY_OBJECT_ARRAY = new ArrayType(AnyObjectType.INSTANCE),
			STRING_ARRAY     = new ArrayType(ClassType.STRING),
			
			BYTE_OR_BOOLEAN_ARRAY = new ArrayType(PrimitiveType.BYTE_BOOLEAN),
			BOOLEAN_ARRAY = new ArrayType(PrimitiveType.BOOLEAN),
			BYTE_ARRAY    = new ArrayType(PrimitiveType.BYTE),
			SHORT_ARRAY   = new ArrayType(PrimitiveType.SHORT),
			CHAR_ARRAY    = new ArrayType(PrimitiveType.CHAR),
			INT_ARRAY     = new ArrayType(PrimitiveType.INT),
			LONG_ARRAY    = new ArrayType(PrimitiveType.LONG),
			FLOAT_ARRAY   = new ArrayType(PrimitiveType.FLOAT),
			DOUBLE_ARRAY  = new ArrayType(PrimitiveType.DOUBLE);


	public final Type memberType, elementType;
	public final int nestingLevel;
	private final String braces;
	
	public ArrayType(String str) {
		this(new ExtendedStringReader(str));
	}
	
	public ArrayType(ExtendedStringReader in) {
		super(ClassType.OBJECT);
		
		in.mark();
		
		final int memberTypeStart = in.getPos();
		
		int nestingLevel = 0;
		
		for(int ch = in.read(); ch == '['; ch = in.read())
			nestingLevel++;
		
		in.prev();
		
		this.nestingLevel = nestingLevel;
		
		this.memberType = parseType(in);
		
		if(nestingLevel == 0)
			throw new InvalidArrayNameException(in);
		
		this.elementType = nestingLevel == 1 ? memberType : new ArrayType(memberType, nestingLevel - 1);
		
		this.braces = "[]".repeat(nestingLevel);
		this.name = memberType.getName() + braces;
		this.encodedName = in.readString(memberTypeStart, in.getPos());
		
		in.unmark();
	}
	
	public ArrayType(String encodedMemberName, int nestingLevel) {
		this(parseType(encodedMemberName), nestingLevel);
	}
	
	public ArrayType(Type memberType) {
		this(memberType, 1);
	}
	
	public ArrayType(Type memberType, int nestingLevel) {
		if(nestingLevel == 0)
			throw new IllegalArgumentException("nestingLevel cannot be zero");
		
		if(memberType instanceof ArrayType arrayMemberType) {
			nestingLevel += arrayMemberType.nestingLevel;
			memberType = arrayMemberType.memberType;
		}
		
		this.nestingLevel = nestingLevel;
		
		this.braces = "[]".repeat(nestingLevel);
		
		this.name = memberType.getName() + braces;
		this.encodedName = "[".repeat(nestingLevel) + memberType.getEncodedName();
		
		this.memberType = memberType;
		this.elementType = nestingLevel == 1 ? memberType : new ArrayType(memberType, nestingLevel - 1);
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return memberType.getName(classinfo) + braces;
	}
	
	@Override
	public String toString() {
		return memberType.toString() + braces;
	}
	
	@Override
	public String getNameForVariable() {
		return (memberType.isPrimitive() ? memberType.getName() : memberType.getNameForVariable()) + "Array";
	}
	
	@Override
	public final boolean isBasicArrayType() {
		return true;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(memberType);
	}
	
	
	@Override
	protected boolean canCastTo(Type other) {
		if(other.equals(ClassType.OBJECT)) {
			return true;
		}
		
		if(other.isBasicArrayType()) {
			ArrayType arrayType = (ArrayType)other;
			
			return (this.nestingLevel == arrayType.nestingLevel && this.memberType.isSubtypeOf(arrayType.memberType))
					|| this.elementType.isSubtypeOf(arrayType.elementType);
		}
		
		return false;
	}
}