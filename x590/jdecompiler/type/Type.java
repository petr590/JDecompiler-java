package x590.jdecompiler.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritableAndImportable;
import x590.jdecompiler.Stringified;
import x590.jdecompiler.exception.IncopatibleTypesException;
import x590.jdecompiler.exception.InvalidMethodDescriptorException;
import x590.jdecompiler.exception.InvalidTypeNameException;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nonnull;
import x590.util.annotation.Nullable;

/**
 * Класс, описывающий тип в Java: int, double, String и т.д.
 */
@Immutable
public abstract class Type implements Stringified, StringWritableAndImportable {
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write(this.toString(classinfo));
	}
	
	@Override
	public abstract String toString();
	
	/** Закодированное имя типа:<br>
	 * "Ljava/lang/Object;", "I".
	 * Используется для сравнения типов и для получения хеш-кода */
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
	
	/** Гарантирует, что объект - экземпляр класса PrimitiveType */
	public boolean isPrimitive() {
		return false;
	}
	
	/** Гарантирует, что объект - экземпляр класса IntegralType */
	public boolean isIntegral() {
		return false;
	}
	
	
	/** Для всех ссылочных типов, в том числе и для специальных */
	public boolean isReferenceType() {
		return isBasicReferenceType();
	}
	
	public boolean isUncertainReferenceType() {
		return false;
	}
	
	
	/** Гарантирует, что объект - экземпляр класса ReferenceType */
	public boolean isBasicReferenceType() {
		return false;
	}
	
	/** Гарантирует, что объект - экземпляр класса ClassType */
	public boolean isBasicClassType() {
		return false;
	}
	
	/** Гарантирует, что объект - экземпляр класса ArrayType */
	public boolean isBasicArrayType() {
		return false;
	}
	
	/** Гарантирует, что объект - экземпляр класса WrapperClassType */
	public boolean isWrapperClassType() {
		return false;
	}
	
	/** Для всех generic типов */
	public boolean isGenericType() {
		return false;
	}
	
	
	/** Проверяет, что это тип {@link PrimitiveType#BYTE}, {@link PrimitiveType#SHORT} или {@link PrimitiveType#CHAR} */
	public final boolean isByteOrShortOrChar() {
		return this == PrimitiveType.BYTE || this == PrimitiveType.SHORT || this == PrimitiveType.CHAR;
	}
	
	/** Проверяет, что это тип {@link PrimitiveType#LONG}, {@link PrimitiveType#FLOAT} или {@link PrimitiveType#DOUBLE} */
	public final boolean isLongOrFloatOrDouble() {
		return this == PrimitiveType.LONG || this == PrimitiveType.FLOAT || this == PrimitiveType.DOUBLE;
	}
	
	
	public final boolean canUseShortOperator() {
		return this.isPrimitive() || this.isWrapperClassType();
	}
	
	
	/** Размер типа на стеке (кратен 4 байтам) */
	public abstract TypeSize getSize();
	
	
	public final boolean isSubtypeOf(Type other) {
		return this.castToNarrowestNoexcept(other) != null;
	}
	
	/** Возвращает {@literal true} если мы можем неявно преобразовать {@literal this} в {@code other}
	 * (например, int -> long. На уровне байткода мы не можем сделать такое преобразование неявно) */
	public boolean isImplicitSubtypeOf(Type other) {
		return isSubtypeOf(other);
	}
	
	
	protected abstract boolean canCastTo(Type other);
	
	
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
	
	
	/** Преобразует тип к наиболее узкому типу (когда мы используем значение как значение какого-то типа) */
	public final Type castToNarrowest(Type other) {
		Type type = castToNarrowestNoexcept(other);
		
		if(type != null) return type;
		
		throw new IncopatibleTypesException(this, other);
	}

	/** Преобразует тип к наиболее широкрму типу (используется при присвоении значения переменной) */
	public final Type castToWidest(Type other) {
		Type type = castToWidestNoexcept(other);
		
		if(type != null) return type;
		
		throw new IncopatibleTypesException(this, other);
	}
	
	
	/** Преобразует тип к общему типу (используется, например, в тернарном операторе) */
	public Type castToGeneral(Type other) {
		Type type = this.castToWidestNoexcept(other);
		
		if(type != null) return type;
		
		type = other.castToWidestNoexcept(this);
		
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
		return this.getEncodedName().hashCode();
	}
	
	
	public final boolean equalsOneOf(Type other1, Type other2) {
		return this.equals(other1) || this.equals(other2);
	}
	
	public final boolean equalsOneOf(Type other1, Type other2, Type other3) {
		return this.equals(other1) || this.equals(other2) || this.equals(other3);
	}
	
	public final boolean equalsOneOf(Type... others) {
		return Arrays.stream(others).anyMatch(other -> this.equals(other));
	}
	
	/** Сравнивает типы без учёта сигнатуры */
	public boolean baseEquals(Type other) {
		return this.equals(other);
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
			case 'L': return ClassType.read(in.next());
			case '[': return ArrayType.read(in);
			case 'T': return new SignatureParameterType(in.next());
			default:
				throw new InvalidTypeNameException(in, in.distanceToMark());
		}
	}
	
	
	public static @Immutable List<Type> parseMethodArguments(String str) {
		return parseMethodArguments(new ExtendedStringReader(str));
	}
	
	public static @Immutable List<Type> parseMethodArguments(ExtendedStringReader in) {
		in.mark();
		
		if(in.read() != '(')
			throw new InvalidMethodDescriptorException(in);
		
		List<Type> arguments = new ArrayList<>();
		
		while(true) {
			if(in.get() == ')') {
				in.incPos();
				in.unmark();
				return Collections.unmodifiableList(arguments);
			}
			
			arguments.add(parseType(in));
		}
	}
	
	
	public static BasicType parseReturnType(String in) {
		return in.charAt(0) == 'V' ? PrimitiveType.VOID : parseType(in);
	}
	
	public static BasicType parseReturnType(ExtendedStringReader in) {
		if(in.get() == 'V') {
			in.incPos();
			return PrimitiveType.VOID;
		}
		
		return parseType(in);
	}
	
	
	public static ReferenceType parseReferenceType(String encodedName) {
		return encodedName.charAt(0) == '[' ? ArrayType.fromDescriptor(encodedName) : ClassType.fromDescriptor(encodedName);
	}
	
	
	public static ReferenceType parseSignatureParameter(ExtendedStringReader in) {
		switch(in.get()) {
			case 'L': return ClassType.read(in.next());
			case '[': return ArrayType.read(in);
			case 'T': return new SignatureParameterType(in.next());
			default:
				throw new InvalidTypeNameException(in, in.distanceToMark());
		}
	}
	
	
	private static final Function<ExtendedStringReader, ReferenceType> signatureParameterGetter =
			in -> switch(in.get()) {
				case '+' -> new ExtendingGenericType(in.next());
				case '-' -> new SuperGenericType(in.next());
				case '*' -> {
					in.incPos();
					yield AnyGenericType.INSTANCE;
				}
				
				case ExtendedStringReader.EOF_CHAR -> throw new InvalidTypeNameException(in, in.distanceToMark());
				
				default -> parseSignatureParameter(in);
			};

	
	public static @Nonnull GenericParameters<ReferenceType> parseSignature(ExtendedStringReader in) {
		return GenericParameters.readNonnull(in, signatureParameterGetter);
	}
	
	public static @Nullable GenericParameters<ReferenceType> parseNullableSignature(ExtendedStringReader in) {
		return GenericParameters.readNullable(in, signatureParameterGetter);
	}
	
	public static @Nullable GenericParameters<GenericParameterType> parseNullableGenericParameters(ExtendedStringReader in) {
		return GenericParameters.readNullable(in, GenericParameterType::new);
	}
}
