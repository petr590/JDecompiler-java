package x590.jdecompiler.type;

import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.exception.InvalidArrayNameException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringReader;

/**
 * Описывает тип массива, включая массив примитивов,
 * многомерные массивы и массивы SpecialType
 */
public final class ArrayType extends ReferenceType {
	
	private static final ClassType ARRAY_SUPER_TYPE = ClassType.OBJECT;
	private static final List<ClassType> ARRAY_INTERFACES = List.of(ClassType.CLONEABLE, ClassType.SERIALIZABLE);
	
	public static final ArrayType
			ANY_ARRAY        = new ArrayType(AnyType.INSTANCE),
			ANY_OBJECT_ARRAY = new ArrayType(AnyObjectType.INSTANCE),
			
			OBJECT_ARRAY     = new ArrayType(ClassType.OBJECT),
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
	

	
	public static ArrayType fromClass(Class<?> clazz) {
		Class<?> componentClass = clazz.getComponentType();
		
		if(componentClass.isPrimitive()) {
			if(componentClass == byte.class) return BYTE_ARRAY;
			if(componentClass == short.class) return SHORT_ARRAY;
			if(componentClass == char.class) return CHAR_ARRAY;
			if(componentClass == int.class) return INT_ARRAY;
			if(componentClass == long.class) return LONG_ARRAY;
			if(componentClass == float.class) return FLOAT_ARRAY;
			if(componentClass == double.class) return DOUBLE_ARRAY;
			if(componentClass == boolean.class) return BOOLEAN_ARRAY;
			if(componentClass == void.class) throw new IllegalArgumentException("Illegal type: array of voids");
			throw new IllegalArgumentException("Cannot recognize Class of primitive type \"" + clazz + "\"");
		}
		
		int nestingLevel = 1;
		
		while(componentClass.isArray()) {
			componentClass = componentClass.getComponentType();
			nestingLevel++;
		}
		
		return new ArrayType(Type.fromClass(componentClass), nestingLevel);
	}
	
	public static ArrayType fromDescriptor(String arrayEncodedName) {
		return new ArrayType(arrayEncodedName);
	}
	
	public static ArrayType read(ExtendedStringReader in) {
		return new ArrayType(in);
	}
	
	
	public static ArrayType forType(Type memberType) {
		return forType(memberType, 1);
	}
	
	public static ArrayType forType(Type memberType, int nestingLevel) {
		return new ArrayType(memberType, nestingLevel);
	}
	
	
	private final Type memberType, elementType;
	private final int nestingLevel;
	private final String braces;
	
	private ArrayType(String arrayEncodedName) {
		this(new ExtendedStringReader(arrayEncodedName));
	}
	
	private ArrayType(ExtendedStringReader in) {
		super(ARRAY_SUPER_TYPE, ARRAY_INTERFACES);
		
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
	
	private ArrayType(Type memberType) {
		this(memberType, 1);
	}
	
	private ArrayType(Type memberType, int nestingLevel) {
		super(ARRAY_SUPER_TYPE, ARRAY_INTERFACES);
		
		if(nestingLevel <= 0) {
			throw new IllegalArgumentException("nestingLevel <= 0");
		}
		
		
		this.elementType = nestingLevel == 1 ? memberType : new ArrayType(memberType, nestingLevel - 1);
		
		if(memberType instanceof ArrayType arrayMemberType) {
			nestingLevel += arrayMemberType.nestingLevel;
			memberType = arrayMemberType.memberType;
		}
		
		this.nestingLevel = nestingLevel;
		
		this.braces = "[]".repeat(nestingLevel);
		
		this.name = memberType.getName() + braces;
		this.encodedName = "[".repeat(nestingLevel) + memberType.getEncodedName();
		
		this.memberType = memberType;
	}
	
	
	public Type getMemberType() {
		return memberType;
	}
	
	public Type getElementType() {
		return elementType;
	}
	
	public int getNestingLevel() {
		return nestingLevel;
	}
	
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printObject(memberType, classinfo).print(braces);
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
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(memberType);
	}
	
	
	@Override
	protected boolean canCastTo(Type other) {
		if(other.equals(ClassType.OBJECT)) {
			return true;
		}
		
		if(other instanceof ArrayType arrayType) {
			return (nestingLevel == arrayType.nestingLevel && memberType.isSubtypeOf(arrayType.memberType))
					|| elementType.isSubtypeOf(arrayType.elementType);
		}
		
		return false;
	}
	
	@Override
	public boolean baseEquals(Type other) {
		return this == other || other instanceof ArrayType arrayType && this.baseEquals(arrayType);
	}
	
	public boolean baseEquals(ArrayType other) {
		return this == other || nestingLevel == other.nestingLevel && memberType.baseEquals(other.memberType) ||
				elementType.baseEquals(other.elementType);
	}
}
