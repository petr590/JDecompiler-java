package x590.javaclass.type;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.Stringified;
import x590.javaclass.exception.IllegalMethodDescriptorException;
import x590.javaclass.exception.IncopatibleTypesException;
import x590.javaclass.exception.InvalidTypeNameException;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.util.Util;

/**
 * Класс, описывающий тип в Java: int, double, String и т.д.
 */
public abstract class Type implements Stringified, StringWritableAndImportable {
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(this.toString(classinfo));
	}
	
	@Override
	public abstract String toString();
	
	/** Закодированное имя типа:<br>
	 * "Ljava/lang/Object;", "I" */
	public abstract String getEncodedName();
	
	/** Имя типа:<br>
	 * "java.lang.Object", "int" */
	public abstract String getName();
	
	/** Имя импортированного типа (если импортирование возможно):<br>
	 * "Object", "int" */
	public String getName(ClassInfo classinfo) {
		return getName();
	}
	
	/** Имя для переменной. Например, все переменные типа int называются "n".
	 * Если таких переменных больше одной, то к названиям добавляется номер. */
	public abstract String getNameForVariable();
	
	
	/**
	 * Является ли тип базовым (т.е. java типом).
	 * Есть некоторые специальные типы, использующиеся при декомпиляции,
	 * такие, как {@link AnyType} (любой тип),
	 * {@link AnyObjectType} (любой ссылочный тип) и др.
	 */
	public abstract boolean isBasic();
	
	public boolean isSpecial() {
		return !isBasic();
	}
	
	/* Гарантирует, что объект - экземпляр класса PrimitiveType */
	public boolean isPrimitive() {
		return false;
	}
	
	/* Гарантирует, что объект - экземпляр класса IntegralType */
	public boolean isIntegral() {
		return false;
	}
	
	/* Гарантирует, что объект - экземпляр класса ReferenceType */
	public boolean isReferenceType() {
		return false;
	}
	
	/* Гарантирует, что объект - экземпляр класса ClassType */
	public boolean isClassType() {
		return false;
	}
	
	/* Гарантирует, что объект - экземпляр класса ArrayType */
	public boolean isArrayType() {
		return false;
	}
	
	/** Размер типа на стеке (кратен 4 байтам) */
	public abstract TypeSize getSize();
	
	
	public final boolean isSubtypeOf(Type other) {
		return this.castToNarrowestNoexcept(other) != null;
	}
	
	
	protected abstract boolean canCastTo(Type other);
	
	
	@Deprecated
	protected boolean canReverse(Type other) {
		return true;
	}
	
	
	protected abstract Type castToNarrowestImpl(Type other);
	
	protected abstract Type castToWidestImpl(Type other);
	
	protected Type reversedCastToNarrowestImpl(Type other) {
		return null;
	}
	
	protected Type reversedCastToWidestImpl(Type other) {
		return null;
	}
	
	
	public final @Nullable Type castToNarrowestNoexcept(Type other) {
		Type type = castToNarrowestImpl(other);
		
		if(type != null) return type;
		
		return other.reversedCastToNarrowestImpl(this);
	}
	
	
	public final @Nullable Type castToWidestNoexcept(Type other) {
		Type type = castToWidestImpl(other);
		
		if(type != null) return type;
		
		return other.reversedCastToWidestImpl(this);
	}
	
	
	public final Type castToNarrowest(Type other) {
		Type type = castToNarrowestNoexcept(other);
		
		if(type != null) return type;
		
		throw new IncopatibleTypesException(this, other);
	}
	
	public final Type castToWidest(Type other) {
		Type type = castToWidestNoexcept(other);
		
		if(type != null) return type;
		
		throw new IncopatibleTypesException(this, other);
	}
	
	
	/** Выполняет сведение типа. */
	public Type reduced() {
		return this;
	}
	
	
	@Override
	public final boolean equals(Object obj) {
		return this == obj || obj instanceof Type other && this.equals(other);
	}
	
	public final boolean equals(Type other) {
		return this == other || (this.getClass() == other.getClass() && this.getEncodedName().equals(other.getEncodedName()));
	}
	
	@Override
	public final int hashCode() {
		return this.getClass().hashCode() ^ this.getEncodedName().hashCode();
	}
	
	
	public int implicitCastStatus(Type other) {
		return this == other ? CastStatus.SAME : this.canCastTo(other) ? CastStatus.EXTEND : CastStatus.NONE;
	}
	
	
	
	public static BasicType parseType(String str) {
		return parseType(new ExtendedStringReader(str));
	}
	
	public static BasicType parseType(ExtendedStringReader in) {
		switch(in.get()) {
			case 'B': in.incPos(); return PrimitiveType.BYTE;
			case 'C': in.incPos(); return PrimitiveType.CHAR;
			case 'S': in.incPos(); return PrimitiveType.SHORT;
			case 'I': in.incPos(); return PrimitiveType.INT;
			case 'J': in.incPos(); return PrimitiveType.LONG;
			case 'F': in.incPos(); return PrimitiveType.FLOAT;
			case 'D': in.incPos(); return PrimitiveType.DOUBLE;
			case 'Z': in.incPos(); return PrimitiveType.BOOLEAN;
			case 'L': return new ClassType(in.next());
			case '[': return new ArrayType(in);
			case 'T': return new ParameterType(in.next());
			default:
				throw new InvalidTypeNameException(in);
		}
	}
	
	
	public static List<Type> parseMethodArguments(String str) {
		return parseMethodArguments(new ExtendedStringReader(str));
	}
	
	public static List<Type> parseMethodArguments(ExtendedStringReader in) {
		in.mark();
		
		if(in.read() != '(')
			throw new IllegalMethodDescriptorException(in);
		
		List<Type> arguments = new ArrayList<>();
		
		while(true) {
			if(in.get() == ')') {
				in.incPos();
				in.unmark();
				return arguments;
			}
			
			arguments.add(parseType(in));
		}
	}
	
	
	public static BasicType parseReturnType(String in) {
		return in.charAt(0) == 'V' ? PrimitiveType.VOID : parseType(in);
	}
	
	public static BasicType parseReturnType(ExtendedStringReader in) {
		return in.get() == 'V' ? PrimitiveType.VOID : parseType(in);
	}
	
	
	public static ReferenceType parseReferenceType(String in) {
		return in.charAt(0) == '[' ? new ArrayType(in) : ClassType.valueOf(in);
	}
	
	
	public static ReferenceType parseParameter(ExtendedStringReader in) {
		switch(in.get()) {
			case 'L': return new ClassType(in.next());
			case '[': return new ArrayType(in);
			case 'T': return new ParameterType(in.next());
			default:
				throw new InvalidTypeNameException(in);
		}
	}
	
	
	public static List<ReferenceType> parseParameters(ExtendedStringReader in) {
		in.mark();
		
		if(in.read() != '<')
			throw new InvalidTypeNameException(in, 0);
		
		List<ReferenceType> parameters = new ArrayList<>();
		
		while(true) {
			switch(in.get()) {
				case '>':
					in.incPos();
					in.unmark();
					return parameters;
					
				case '+':
					parameters.add(new ExtendingGenericType(in.next()));
					break;
				case '-':
					parameters.add(new SuperGenericType(in.next()));
					break;
				case '*':
					in.next();
					parameters.add(AnyGenericType.INSTANCE);
					break;
					
				case Util.EOF_CHAR:
					throw new InvalidTypeNameException(in, in.distanceToMark());
					
				default:
					parameters.add(parseParameter(in));
			}
		}
	}		
}