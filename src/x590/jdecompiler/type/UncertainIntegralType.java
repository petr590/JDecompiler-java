package x590.jdecompiler.type;

import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.primitive.IntegralType;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.util.WhitespaceStringBuilder;

import static x590.jdecompiler.type.primitive.PrimitiveType.CHAR_CAPACITY;

import x590.jdecompiler.clazz.ClassInfo;

/**
 * Класс, который представляет возможный целочисленный тип
 * размером не больше 32 бит ({@literal byte}, {@literal short}, {@literal int}, {@literal char}, {@literal boolean}).
 * Хранит минимальный и максимальный размер типа, а также два флага,
 * которые отвечают за типы {@literal boolean} и {@literal char}.
 */
public final class UncertainIntegralType extends Type {
	
	private static final UncertainIntegralType[] INSTANCES = new UncertainIntegralType[64];
	
	public static final int INCLUDE_BOOLEAN = 0x1, INCLUDE_CHAR = 0x2;
	
	
	private final int minCapacity, maxCapacity;
	private final boolean includeBoolean, includeChar;
	
	
	public int minCapacity() {
		return minCapacity;
	}
	
	public int maxCapacity() {
		return maxCapacity;
	}
	
	public boolean includeBoolean() {
		return includeBoolean;
	}
	
	public boolean includeChar() {
		return includeChar;
	}
	
	
	private final PrimitiveType highPrimitiveType;
	
	private final String encodedName;
	
	private static PrimitiveType primitiveTypeByCapacity(int capacity, boolean includeChar) {
		if(includeChar && capacity == CHAR_CAPACITY) {
			return PrimitiveType.CHAR;
		}
		
		switch(capacity) {
			case 1: return PrimitiveType.BYTE;
			case 2: return PrimitiveType.SHORT;
			case 4: return PrimitiveType.INT;
			default:
				throw new IllegalArgumentException("Cannot find " + (includeChar ? "unsigned" : "signed") +
						" integral type for capacity " + capacity);
		}
	}
	
	private UncertainIntegralType(int minCapacity, int maxCapacity, boolean includeBoolean, boolean includeChar) {
		this.minCapacity = minCapacity;
		this.maxCapacity = maxCapacity;
		this.includeBoolean = includeBoolean;
		this.includeChar = includeChar;
		this.highPrimitiveType = primitiveTypeByCapacity(maxCapacity, includeChar);
		this.encodedName = "VariableCapacityIntegralType:" + minCapacity + ":" + maxCapacity + ":" +
				(char)('0' + (includeBoolean ? 1 : 0) + (includeChar ? 2 : 0));
	}
	
	private UncertainIntegralType(int minCapacity, int maxCapacity, int flags) {
		this(minCapacity, maxCapacity, (flags & INCLUDE_BOOLEAN) != 0, (flags & INCLUDE_CHAR) != 0);
	}

	
	private static UncertainIntegralType getInstanceNoexcept(int minCapacity, int maxCapacity, boolean includeBoolean, boolean includeChar) {
		return getInstanceNoexcept(minCapacity, maxCapacity, (includeBoolean ? INCLUDE_BOOLEAN : 0) | (includeChar ? INCLUDE_CHAR: 0));
	}
	
	private static UncertainIntegralType getInstanceNoexcept(int minCapacity, int maxCapacity, int flags) {
		
		if(minCapacity < 1 || minCapacity > 4)
			throw new IllegalArgumentException("minCapacity = " + minCapacity);
		
		if(maxCapacity < 1 || maxCapacity > 4)
			throw new IllegalArgumentException("maxCapacity = " + maxCapacity);
		
		if(minCapacity > maxCapacity)
			return null;
		
		int index = (minCapacity - 1) | (maxCapacity - 1) << 2 | flags << 4;
		
		UncertainIntegralType instance = INSTANCES[index];
		
		if(instance != null)
			return instance;
		
		return INSTANCES[index] = new UncertainIntegralType(minCapacity, maxCapacity, flags);
	}
	
	
	public static UncertainIntegralType getInstance(int minCapacity, int maxCapacity) {
		return getInstance(minCapacity, maxCapacity, false, false);
	}
	
	public static UncertainIntegralType getInstance(int minCapacity, int maxCapacity, boolean includeBoolean, boolean includeChar) {
		return getInstance(minCapacity, maxCapacity, (includeBoolean ? INCLUDE_BOOLEAN : 0) | (includeChar ? INCLUDE_CHAR: 0));
	}
	
	public static UncertainIntegralType getInstance(int minCapacity, int maxCapacity, int flags) {
		var type = getInstanceNoexcept(minCapacity, maxCapacity, flags);
		
		if(type != null)
			return type;
		
		throw new IllegalArgumentException("minCapacity = " + minCapacity + ", maxCapacity = " + maxCapacity + ", flags = " + flags);
	}
	
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.printObject(reduced(), classinfo);
	}
	
	@Override
	public String toString() {
		WhitespaceStringBuilder typesStr = new WhitespaceStringBuilder();
		
		if(minCapacity <= 1 && maxCapacity >= 1)
			typesStr.append("byte");
		
		if(minCapacity <= 2 && maxCapacity >= 2)
			typesStr.append("short");
		
		if(minCapacity <= 4 && maxCapacity >= 4)
			typesStr.append("int");
		
		if(includeChar)
			typesStr.append("char");
		
		if(includeBoolean)
			typesStr.append("boolean");
		
		return "(" + typesStr + ")";
	}
	
	@Override
	public String getEncodedName() {
		return encodedName;
	}
	
	@Override
	public String getName() {
		return highPrimitiveType.getName();
	}
	
	@Override
	public String getNameForVariable() {
		return highPrimitiveType.getNameForVariable();
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	protected boolean canCastToNarrowest(Type other) {
		if(this == other || (other == PrimitiveType.BOOLEAN && includeBoolean) || other == highPrimitiveType) {
			return true;
		}
		
		if(other == PrimitiveType.CHAR) {
			return includeChar || maxCapacity > CHAR_CAPACITY;
		}
		
		if(other instanceof IntegralType integralType) {
			return integralType.getCapacity() >= minCapacity;
		}
		
		if(other instanceof UncertainIntegralType integralType) {
			return integralType.maxCapacity >= minCapacity;
		}
		
		return false;
	}
	
	
	private static Type castImpl0(UncertainIntegralType type, Type other, boolean widest) {
		
		if(other.isPrimitive()) {
			
			if(other == PrimitiveType.BOOLEAN)
				return type.includeBoolean ? other : null;
			
			if(other == PrimitiveType.CHAR)
				return widest && type.maxCapacity > CHAR_CAPACITY ?
						getInstanceNoexcept(CHAR_CAPACITY * 2, type.maxCapacity, false, type.includeChar) :  
						type.includeChar ? other : null;
			
			if(other instanceof IntegralType integralType) {
				int capacity = integralType.getCapacity();
				
				if(widest)
					return getInstanceNoexcept(Math.max(capacity, type.minCapacity), type.maxCapacity, 0);
				
				return getInstanceNoexcept(type.minCapacity, Math.min(capacity, type.maxCapacity), false, type.includeChar && capacity > CHAR_CAPACITY);
			}
			
		} else if(other instanceof UncertainIntegralType integralType) {
			return castImpl0(type, integralType, widest);
		}
		
		return null;
	}
	
	private static Type castImpl0(UncertainIntegralType type, UncertainIntegralType other, boolean widest) {
		int minCapacity = Math.max(type.minCapacity, other.minCapacity),
			maxCapacity = Math.min(other.maxCapacity, type.maxCapacity);
		
		if(widest) {
			return getInstanceNoexcept(minCapacity, minCapacity >= maxCapacity ? type.maxCapacity : maxCapacity,
					type.includeBoolean && other.includeBoolean, type.includeChar && other.includeChar);
			
		} else {
			return getInstanceNoexcept(minCapacity >= maxCapacity ? type.minCapacity : minCapacity, maxCapacity,
					type.includeBoolean && other.includeBoolean,
					type.includeChar && (other.includeChar || maxCapacity > CHAR_CAPACITY));
		}
	}
	
	private static Type reversedCastImpl0(UncertainIntegralType type, Type other, boolean widest) {
		
		if(other.isPrimitive()) {
			
			if(other == PrimitiveType.BOOLEAN)
				return type.includeBoolean ? other : null;
			
			if(other == PrimitiveType.CHAR)
				return type.includeChar || !widest && type.maxCapacity > CHAR_CAPACITY ? other : null;
			
			if(other instanceof IntegralType integralType) {
				int capacity = integralType.getCapacity();
				
				if(widest ? capacity >= type.minCapacity : capacity <= type.maxCapacity) {
					return other;
				}
			}
		}
		
		return null;
	}
	
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		return castImpl0(this, other, kind.toBoolean());
	}
	
	@Override
	protected Type reversedCastImpl(Type other, CastingKind kind) {
		return reversedCastImpl0(this, other, kind.toBoolean());
	}
	
	@Override
	public boolean isImplicitSubtypeOf(Type other) {
		return reduced().isImplicitSubtypeOf(other);
	}
	
	
	/** Возвращает верхнюю границу типа или boolean, если установлен флаг {@link #includeBoolean()} */
	@Override
	public BasicType reduced() {
		return includeBoolean ? PrimitiveType.BOOLEAN : highPrimitiveType;
	}
}
