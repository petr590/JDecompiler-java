package x590.javaclass.type;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.ClassInfo;
import x590.javaclass.util.WhitespaceStringBuilder;
import x590.javaclass.util.Util;

public class VariableCapacityIntegralType extends SpecialType {
	
	private static final List<VariableCapacityIntegralType> instances = new ArrayList<>();
	
	public static final int INCLUDE_BOOLEAN = 0x1, INCLUDE_CHAR = 0x2;
	public static final int CHAR_CAPACITY = 2;
	
	
	public final int minCapacity, maxCapacity;
	public final boolean includeBoolean, includeChar;
	
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
				throw new IllegalStateException("Cannot find " + (includeChar ? "unsigned" : "signed") +
						" integral type for capacity " + capacity);
		}
	}
	
	private VariableCapacityIntegralType(int minCapacity, int maxCapacity, boolean includeBoolean, boolean includeChar) {
		this.minCapacity = minCapacity;
		this.maxCapacity = maxCapacity;
		this.includeBoolean = includeBoolean;
		this.includeChar = includeChar;
		this.highPrimitiveType = primitiveTypeByCapacity(maxCapacity, includeChar);
		this.encodedName = "SVariableCapacityIntegralType:" + minCapacity + ":" + maxCapacity + ":" +
				(char)('0' + (includeBoolean ? 1 : 0) + (includeChar ? 2 : 0));
	}
	
	public static VariableCapacityIntegralType getInstance(int minCapacity, int maxCapacity, boolean includeBoolean, boolean includeChar) {
		
		if(minCapacity > maxCapacity)
			return null;
		
		for(VariableCapacityIntegralType instance : instances) {
			if(instance.minCapacity == minCapacity && instance.maxCapacity == maxCapacity &&
				instance.includeBoolean == includeBoolean && instance.includeChar == includeChar) {
				
				return instance;
			}
		}
		
		VariableCapacityIntegralType instance = new VariableCapacityIntegralType(minCapacity, maxCapacity, includeBoolean, includeChar);
		instances.add(instance);
		return instance;
	}
	
	
	public static VariableCapacityIntegralType getInstance(int minCapacity, int maxCapacity) {
		return getInstance(minCapacity, maxCapacity, 0);
	}
	
	public static VariableCapacityIntegralType getInstance(int minCapacity, int maxCapacity, int flags) {
		var type = getInstanceNoexcept(minCapacity, maxCapacity, flags);
		
		if(type != null)
			return type;
		
		throw new IllegalArgumentException(
				"minCapacity = " + minCapacity + ", maxCapacity = " + maxCapacity + ", flags = " + flags);
	}
	
	
	private static VariableCapacityIntegralType getInstanceNoexcept(int minCapacity, int maxCapacity, int flags) {
		VariableCapacityIntegralType instance = getInstance(minCapacity, maxCapacity, (flags & INCLUDE_BOOLEAN) != 0, (flags & INCLUDE_CHAR) != 0);
		
		if(instance == null)
			throw new IllegalArgumentException(
				"minCapacity = " + minCapacity + ", maxCapacity = " + maxCapacity + ", flags = 0x" + Util.hex1(flags));
		
		return instance;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return highPrimitiveType.toString(classinfo);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder().append('(');
		
		WhitespaceStringBuilder typesStr = new WhitespaceStringBuilder();
		
		if(minCapacity <= 1 && maxCapacity >= 1)
			typesStr.append("byte");
		
		if(minCapacity <= 2 && maxCapacity >= 2)
			typesStr.append("short");
		
		if(minCapacity <= 4 && maxCapacity >= 4)
			typesStr.append("int");
		
		if(includeBoolean)
			typesStr.append("boolean");
		
		if(includeChar)
			typesStr.append("char");
		
		return str.append(typesStr).append(')').toString();
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
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	protected boolean isSubtypeOfImpl(Type other) {
		if(this == other || (other == PrimitiveType.BOOLEAN && includeBoolean) || other == highPrimitiveType)
			return true;
		
		if(other == PrimitiveType.CHAR)
			return includeChar || maxCapacity > CHAR_CAPACITY;
		
		if(other.isIntegral()) {
			int capacity = ((IntegralType)other).getCapacity();
			return capacity >= minCapacity;
		}
		
		if(other instanceof VariableCapacityIntegralType integralType) {
			return integralType.maxCapacity >= minCapacity;
		}
		
		return false;
	}
	
// TODO
//	@Override
//	public boolean isStrictSubtypeOf(Type other) {
//		if(this == other || (minCapacity == maxCapacity && other == highPrimitiveType && !includeBoolean && !includeChar))
//			return true;
//
//		if(minCapacity == maxCapacity && other.isIntegral())
//			return ((IntegralType)other).getCapacity() == minCapacity;
//
//		return false;
//	}
	
	private static Type castImpl0(VariableCapacityIntegralType type, Type other, boolean widest) {
		
		if(other.isPrimitive()) {
			
			if(other == PrimitiveType.BOOLEAN)
				return type.includeBoolean ? other : null;
			
			if(other == type.highPrimitiveType)
				return widest ? other : type;
			
			if(other == PrimitiveType.CHAR)
				return type.includeChar ? other : null;
			
			if(other.isIntegral()) {
				int capacity = ((IntegralType)other).getCapacity();
				
				if(capacity == type.minCapacity)
					return widest ? type : other;
				
				if(capacity == type.maxCapacity)
					return widest ? other : type;
				
				if(capacity > type.minCapacity)
					return getInstance(type.minCapacity, Math.min(capacity, type.maxCapacity), false, type.includeChar && capacity > CHAR_CAPACITY);
			}
		}
		
		if(other instanceof VariableCapacityIntegralType integralType) {
			return castImpl0(type, integralType, widest);
		}
		
		return null;
	}
	
	private static Type castImpl0(VariableCapacityIntegralType type, VariableCapacityIntegralType other, boolean widest) {
		int minCapacity = Math.max(type.minCapacity, other.minCapacity),
			maxCapacity = Math.min(other.maxCapacity, type.maxCapacity);
		
		if(widest) {
			return getInstance(minCapacity, minCapacity >= maxCapacity ? type.maxCapacity : maxCapacity,
					type.includeBoolean && other.includeBoolean, type.includeChar && other.includeChar);
			
		} else {
			return getInstance(minCapacity >= maxCapacity ? type.minCapacity : minCapacity, maxCapacity,
					type.includeBoolean && other.includeBoolean,
					type.includeChar && (other.includeChar || maxCapacity > CHAR_CAPACITY));
		}
	}
	
	private static Type reversedCastImpl0(VariableCapacityIntegralType type, Type other, boolean widest) {
		
		if(other.isPrimitive()) {
			
			if(other == PrimitiveType.BOOLEAN)
				return type.includeBoolean ? other : null;
			
			if(other == type.highPrimitiveType)
				return widest ? type : other;
			
			if(other == PrimitiveType.CHAR)
				return type.includeChar || type.maxCapacity > CHAR_CAPACITY ?
						(widest ? getInstance(CHAR_CAPACITY * 2, type.maxCapacity, false, type.includeChar) : other) : null;
				
			if(other.isIntegral()) {
				int capacity = ((IntegralType)other).getCapacity();
				
				if(widest ? capacity <= type.minCapacity : capacity >= type.maxCapacity)
					return type;
				
				if(widest ? capacity <= type.maxCapacity : capacity >= type.minCapacity) {
					return widest ? getInstance(Math.max(capacity, type.minCapacity), capacity, false, type.includeChar) :
									getInstance(capacity, Math.min(capacity, type.maxCapacity), false, type.includeChar);
				}
			}
		}
		
		return null;
	}
	
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return castImpl0(this, other, false);
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return castImpl0(this, other, true);
	}
	
	
	@Override
	protected Type reversedCastToNarrowestImpl(Type other) {
		return reversedCastImpl0(this, other, false);
	}
	
	@Override
	protected Type reversedCastToWidestImpl(Type other) {
		return reversedCastImpl0(this, other, true);
	}

//	Type getReducedType() {
//		return includeBoolean ? BOOLEAN : highPrimitiveType;
//	}
}