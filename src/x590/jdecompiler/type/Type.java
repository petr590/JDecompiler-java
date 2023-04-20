package x590.jdecompiler.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.IncopatibleTypesException;
import x590.jdecompiler.exception.InvalidMethodDescriptorException;
import x590.jdecompiler.exception.InvalidTypeNameException;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.writable.BiStringifyWritable;
import x590.jdecompiler.writable.SameDisassemblingStringifyWritable;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nonnull;
import x590.util.annotation.Nullable;

/**
 * Класс, описывающий тип в Java: int, double, String и т.д.
 */

@Immutable
public abstract class Type implements
		SameDisassemblingStringifyWritable<ClassInfo>, BiStringifyWritable<ClassInfo, String>, Importable {
	
	/**
	 * Записывает себя и имя переменной через пробел.
	 * Если включены c-style массивы, то массив записывается как в C
	 */
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo, String name) {
		writeLeftDefinition(out, classinfo);
		out.printsp().write(name);
		writeRightDefinition(out, classinfo);
	}
	
	/** Записывает левую часть объявления массива, если включены c-style массивы.
	 * Если нет, то работает просто как {@link #writeTo(StringifyOutputStream, ClassInfo)} */
	public void writeLeftDefinition(StringifyOutputStream out, ClassInfo classinfo) {
		writeTo(out, classinfo);
	}
	
	/** Записывает правую часть объявления массива, если включены c-style массивы.
	 * Если нет, то ничего не делает */
	public void writeRightDefinition(StringifyOutputStream out, ClassInfo classinfo) {}
	
	
	/** @return Самый вложенный тип массива, если {@code JDecompiler.getInstance().useCStyleArray() == true}.
	 * Иначе возвращает {@literal this}. Для всех типов, не являющихся массивами, возвращает {@literal this} */
	public Type getArrayMemberIfUsingCArrays() {
		return this;
	}
	
	
	@Override
	public abstract String toString();
	
	/** @return Закодированное имя типа: "Ljava/lang/Object;", "I".<br>
	 * Используется для сравнения типов и для получения хеш-кода */
	public abstract String getEncodedName();
	
	/** @return Имя типа: "java.lang.Object", "int" */
	public abstract String getName();
	
	/** Имя для переменной. Например, все переменные типа int называются "n".
	 * Если таких переменных больше одной, то к названиям добавляется номер. */
	public abstract String getNameForVariable();
	
	
	/**
	 * Является ли тип базовым (т.е. java типом).
	 * Есть некоторые специальные типы, использующиеся при декомпиляции,
	 * такие, как {@link AnyType} (любой тип),
	 * {@link AnyObjectType} (любой ссылочный тип) и др.
	 */
	public final boolean isBasic() {
		return this instanceof BasicType;
	}
	
	/** Гарантирует, что объект - экземпляр класса PrimitiveType */
	public final boolean isPrimitive() {
		return this instanceof PrimitiveType;
	}
	
	/** Гарантирует, что объект - экземпляр класса IntegralType */
	public final boolean isIntegral() {
		return this instanceof IntegralType;
	}
	
	
	/** Для всех ссылочных типов, в том числе и для специальных */
	public boolean isAnyReferenceType() {
		return isReferenceType();
	}
	
	
	/** Гарантирует, что объект - экземпляр класса ReferenceType */
	public final boolean isReferenceType() {
		return this instanceof ReferenceType;
	}
	
	/** Гарантирует, что объект - экземпляр класса ClassType */
	public final boolean isClassType() {
		return this instanceof ClassType;
	}
	
	/** Гарантирует, что объект - экземпляр класса WrapperClassType */
	public final boolean isWrapperClassType() {
		return this instanceof WrapperClassType;
	}
	
	/** Гарантирует, что объект - экземпляр класса ArrayType */
	public final boolean isArrayType() {
		return this instanceof ArrayType;
	}
	
	/** Гарантирует, что объект - экземпляр класса UncertainReferenceType */
	public final boolean isUncertainReferenceType() {
		return this instanceof UncertainReferenceType;
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
	
	
	/** Размер типа на стеке */
	public abstract TypeSize getSize();
	
	
	public final boolean isSubtypeOf(Type other) {
		return this.castToNarrowestNoexcept(other) != null;
	}
	
	/** @return {@literal true} если мы можем неявно преобразовать {@code this} в {@code other}. Например,
	 * {@literal int} -> {@literal long}. На уровне байткода мы не можем сделать такое преобразование неявно. */
	public boolean isImplicitSubtypeOf(Type other) {
		return isSubtypeOf(other);
	}
	
	
	protected abstract boolean canCastTo(Type other);
	
	
	/** Реализация метода преобразования к наиболее узкому типу.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно */
	protected abstract Type castToNarrowestImpl(Type other);
	
	/** Реализация метода преобразования к наиболее широкому типу.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно */
	protected abstract Type castToWidestImpl(Type other);
	
	/** Реализация метода преобразования к наиболее узкому типу.
	 * Вызывается, если метод {@link #castToNarrowestImpl(Type)} вернул {@literal null}.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно.
	 * Реализация по умолчанию возвращает {@literal null} */
	protected Type reversedCastToNarrowestImpl(Type other) {
		return null;
	}
	
	/** Реализация метода преобразования к наиболее широкому типу.
	 * Вызывается, если метод {@link #castToWidestImpl(Type)} вернул {@literal null}.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно.
	 * Реализация по умолчанию возвращает {@literal null} */
	protected Type reversedCastToWidestImpl(Type other) {
		return null;
	}
	
	
	/** Преобразует тип к наиболее узкому типу.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно */
	public final @Nullable Type castToNarrowestNoexcept(Type other) {
		Type type = castToNarrowestImpl(other);
		
		if(type != null) return type;
		
		return other.reversedCastToNarrowestImpl(this);
	}
	
	
	/** Преобразует тип к наиболее широкому типу.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно */
	public final @Nullable Type castToWidestNoexcept(Type other) {
		Type type = castToWidestImpl(other);
		
		if(type != null) return type;
		
		return other.reversedCastToWidestImpl(this);
	}
	
	
	/** Преобразует тип к наиболее узкому типу (когда мы используем значение как значение какого-то типа)
	 * @return результат преобразования
	 * @throws IncopatibleTypesException, если преобразование невозможно */
	public final Type castToNarrowest(Type other) {
		Type type = castToNarrowestNoexcept(other);
		
		if(type != null) return type;
		
		throw new IncopatibleTypesException(this, other);
	}
	
	/** Преобразует тип к наиболее широкрму типу (используется при присвоении значения переменной)
	 * @return результат преобразования
	 * @throws IncopatibleTypesException, если преобразование невозможно */
	public final Type castToWidest(Type other) {
		Type type = castToWidestNoexcept(other);
		
		if(type != null) return type;
		
		throw new IncopatibleTypesException(this, other);
	}
	
	
	/** Преобразует тип к общему типу (используется, например, в тернарном операторе)
	 * @return результат преобразования
	 * @throws IncopatibleTypesException, если преобразование невозможно */
	public final Type castToGeneral(Type other, GeneralCastingKind kind) {
		Type type = this.castToGeneralNoexcept(other, kind);
		
		if(type != null)
			return type;
		
		throw new IncopatibleTypesException(this, other);
	}
	
	
	/** Преобразует тип к общему типу. Может мыть переопределён в подклассах
	 * @return результат преобразования или {@literal null}, если преобразование невозможно */
	public @Nullable Type castToGeneralNoexcept(Type other, GeneralCastingKind kind) {
		Type type = this.castToWidestNoexcept(other);
		
		if(type != null)
			return type;
		
		return other.castToWidestNoexcept(this);
	}
	
	
	/** Преобразует тип к общему типу.
	 * @return результат преобразования или {@literal null}, если преобразование невозможно */
	public @Nullable Type implicitCastToGeneralNoexcept(Type other, GeneralCastingKind kind) {
		Type type = castToGeneralNoexcept(other, kind);
		
		if(type != null)
			return type;
		
		if(this.isImplicitSubtypeOf(other))
			return other;
		
		if(other.isImplicitSubtypeOf(this))
			return this;
		
		return null;
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
		return this == other ||
				this.getClass() == other.getClass() &&
				this.getEncodedName().equals(other.getEncodedName());
	}
	
	@Override
	public final int hashCode() {
		return getEncodedName().hashCode();
	}
	
	
	public final boolean equalsOneOf(Type other1, Type other2) {
		return this.equals(other1) || this.equals(other2);
	}
	
	public final boolean equalsOneOf(Type other1, Type other2, Type other3) {
		return this.equals(other1) || this.equals(other2) || this.equals(other3);
	}
	
	public final boolean equalsOneOf(Type other1, Type other2, Type other3, Type... others) {
		return equalsOneOf(other1, other2, other3) || Arrays.stream(others).anyMatch(this::equals);
	}
	
	/** Сравнивает типы без учёта сигнатуры */
	public boolean equalsIgnoreSignature(Type other) {
		return this.equals(other);
	}
	
	
	/** @return статус неявного преобразование к типу.
	 * (для компилятора, т.е. мы можем конвертировать int в long в коде, но не можем сделать это на уровне байткода).
	 * Чем меньше статус, тем выше приоритет. Если статус больше или равен {@link CastStatus.NONE}, значит преобразование невозможно */
	public int implicitCastStatus(Type other) {
		return this == other ? CastStatus.SAME : this.canCastTo(other) ? CastStatus.EXTEND : CastStatus.NONE;
	}
	
	
	public static Type fromClass(Class<?> clazz) {
		
		if(clazz.isPrimitive()) {
			if(clazz == byte.class) return PrimitiveType.BYTE;
			if(clazz == short.class) return PrimitiveType.SHORT;
			if(clazz == char.class) return PrimitiveType.CHAR;
			if(clazz == int.class) return PrimitiveType.INT;
			if(clazz == long.class) return PrimitiveType.LONG;
			if(clazz == float.class) return PrimitiveType.FLOAT;
			if(clazz == double.class) return PrimitiveType.DOUBLE;
			if(clazz == boolean.class) return PrimitiveType.BOOLEAN;
			if(clazz == void.class) return PrimitiveType.VOID;
			throw new IllegalArgumentException("Cannot recognize Class of primitive type \"" + clazz + "\"");
		}
		
		if(clazz.isArray()) {
			return ArrayType.fromClass(clazz);
		}
		
		return ClassType.fromClass(clazz);
	}
	
	
	/** @see #parseType(ExtendedStringInputStream) */
	public static BasicType parseType(String str) {
		return parseType(new ExtendedStringInputStream(str));
	}
	
	/** Парсит любой тип, кроме {@link PrimitiveType#VOID} */
	public static BasicType parseType(ExtendedStringInputStream in) {
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
	
	
	/** @see #parseMethodArguments(ExtendedStringInputStream) */
	public static @Immutable List<Type> parseMethodArguments(String str) {
		return parseMethodArguments(new ExtendedStringInputStream(str));
	}
	
	/** Парсит сигнатуру метода, возвращает список аргументов */
	public static @Immutable List<Type> parseMethodArguments(ExtendedStringInputStream in) {
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
	
	
	/** Парсит возвращаемый тип метода, который может быть {@link PrimitiveType#VOID} */
	public static BasicType parseReturnType(String in) {
		return in.charAt(0) == 'V' ? PrimitiveType.VOID : parseType(in);
	}
	
	/** Парсит возвращаемый тип метода, который может быть {@link PrimitiveType#VOID} */
	public static BasicType parseReturnType(ExtendedStringInputStream in) {
		if(in.get() == 'V') {
			in.incPos();
			return PrimitiveType.VOID;
		}
		
		return parseType(in);
	}
	
	
	/** Парсит тип массива или класса (без префикса 'L') */
	public static ReferenceType parseReferenceType(String encodedName) {
		return encodedName.charAt(0) == '[' ?
				ArrayType.fromDescriptor(encodedName) :
				ClassType.fromDescriptor(encodedName);
	}
	
	
	/** Парсит тип массива, класса или параметра */
	public static ReferenceType parseSignatureParameter(ExtendedStringInputStream in) {
		switch(in.get()) {
			case 'L': return ClassType.read(in.next());
			case '[': return ArrayType.read(in);
			case 'T': return new SignatureParameterType(in.next());
			default:
				throw new InvalidTypeNameException(in, in.distanceToMark());
		}
	}
	
	
	private static final Function<ExtendedStringInputStream, ReferenceType> signatureParameterGetter =
			in -> switch(in.get()) {
				case '+' -> new ExtendingGenericType(in.next());
				case '-' -> new SuperGenericType(in.next());
				case '*' -> {
					in.incPos();
					yield AnyGenericType.INSTANCE;
				}
				
				case ExtendedStringInputStream.EOF_CHAR -> throw new InvalidTypeNameException(in, in.distanceToMark());
				
				default -> parseSignatureParameter(in);
			};
	
	
	public static @Nonnull GenericParameters<ReferenceType> parseSignature(ExtendedStringInputStream in) {
		return GenericParameters.readNonnull(in, signatureParameterGetter);
	}
	
	public static @Nullable GenericParameters<ReferenceType> parseNullableSignature(ExtendedStringInputStream in) {
		return GenericParameters.readNullable(in, signatureParameterGetter);
	}
	
	public static @Nullable GenericParameters<GenericParameterType> parseNullableGenericParameters(ExtendedStringInputStream in) {
		return GenericParameters.readNullable(in, GenericParameterType::new);
	}
}
