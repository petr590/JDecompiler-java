package x590.jdecompiler.type;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.exception.InvalidClassNameException;
import x590.jdecompiler.exception.InvalidTypeNameException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

import static x590.jdecompiler.io.ExtendedStringInputStream.EOF_CHAR;

/**
 * Описывает тип java класса, самого обычного класса
 */
public class ClassType extends ReferenceType {
	
	/**
	 * Определяет, какой это класс: обычный, вложенный, анонимный, package-info, module-info
	 */
	private enum ClassKind {
		PLAIN("plain", false),
		NESTED("nested", false),
		ANONYMOUS("anonymous", false),
		PACKAGE_INFO("package-info", true),
		MODULE_INFO("module-info", true);
		
		private final String name;
		private final boolean isSpecial;
		
		private ClassKind(String name, boolean isSpecial) {
			this.name = name;
			this.isSpecial = isSpecial;
		}
		
		public boolean isSpecial() {
			return isSpecial;
		}
		
		public boolean isPlain() {
			return !isSpecial;
		}
		
		public boolean isNested() {
			return this == NESTED || this == ANONYMOUS;
		}
		
		public boolean isAnonymous() {
			return this == ANONYMOUS;
		}
		
		public boolean isPackageInfo() {
			return this == PACKAGE_INFO;
		}
		
		public boolean isModuleInfo() {
			return this == MODULE_INFO;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	
	private static final Map<String, ClassType> CLASS_TYPES = new HashMap<>();
	
	// Debug
	private static final List<ClassType> ALL_CLASSES = new ArrayList<>();
	
	
	public static final ClassType
			OBJECT         = initFromClass(Object.class),
			STRING         = initFromClass(String.class),
			CLASS          = initFromClass(Class.class),
			ENUM           = initFromClass(Enum.class),
			RECORD         = initFromClass(Record.class),
			THROWABLE      = initFromClass(Throwable.class),
			METHOD_TYPE    = initFromClass(MethodType.class),
			METHOD_HANDLE  = initFromClass(MethodHandle.class),
			STRING_BUILDER = initFromClass(StringBuilder.class),
			
			ANNOTATION   = initFromClass(Annotation.class),
			OVERRIDE     = initFromClass(Override.class),
			CLONEABLE    = initFromClass(Cloneable.class),
			SERIALIZABLE = initFromClass(Serializable.class),
			
			BYTE      = initWrapperFromClass(Byte.class, PrimitiveType.BYTE),
			SHORT     = initWrapperFromClass(Short.class, PrimitiveType.SHORT),
			CHARACTER = initWrapperFromClass(Character.class, PrimitiveType.CHAR),
			INTEGER   = initWrapperFromClass(Integer.class, PrimitiveType.INT),
			LONG      = initWrapperFromClass(Long.class, PrimitiveType.LONG),
			FLOAT     = initWrapperFromClass(Float.class, PrimitiveType.FLOAT),
			DOUBLE    = initWrapperFromClass(Double.class, PrimitiveType.DOUBLE),
			BOOLEAN   = initWrapperFromClass(Boolean.class, PrimitiveType.BOOLEAN),
			VOID      = initWrapperFromClass(Void.class, PrimitiveType.VOID);
	
	
	// Debug
	public static @Immutable Map<String, ClassType> classTypes() {
		return Collections.unmodifiableMap(CLASS_TYPES);
	}
	
	// Debug
	public static @Immutable List<ClassType> allClassTypes() {
		return Collections.unmodifiableList(ALL_CLASSES);
	}
	
	
	private static ClassType initFromClass(Class<?> clazz) {
		String classEncodedName = encodedNameForClass(clazz);
		return addToMap(classEncodedName, clazz, new ClassType(classEncodedName, clazz));
	}
	
	private static WrapperClassType initWrapperFromClass(Class<?> clazz, PrimitiveType type) {
		String classEncodedName = encodedNameForClass(clazz);
		return addToMap(classEncodedName, clazz, new WrapperClassType(classEncodedName, clazz, type));
	}
	
	private static <T extends ClassType> T addToMap(String classEncodedName, Class<?> clazz, T classType) {
		var prev = CLASS_TYPES.put(classEncodedName, classType);
		assert prev == null;
		
		return classType;
	}
	
	
	private static String encodedNameForClass(Class<?> clazz) {
		String encodedName = clazz.descriptorString();
		return encodedName.substring(1, encodedName.length() - 1);
	}
	
	
	/** Не получается через {@link Map#computeIfAbsent(Object, Function)}
	 * из-за того, что иногда надо рекурсивно создавать ClassType */
	private static ClassType getOrCreateClassType(String classEncodedName, Function<String, ClassType> creator) {
		ClassType classType = CLASS_TYPES.get(classEncodedName);
		
		if(classType != null)
			return classType;
		
		classType = creator.apply(classEncodedName);
		CLASS_TYPES.put(classEncodedName, classType);
		return classType;
	}
	
	
	public static ClassType fromClass(Class<?> clazz) {
		return getOrCreateClassType(encodedNameForClass(clazz), encodedName -> new ClassType(encodedName, clazz));
	}
	
	
	public static ClassType fromConstant(ClassConstant clazz) {
		return fromDescriptor(clazz.getNameConstant().getString());
	}
	
	
	/** Принимает строку без префикса 'L', т.е. в виде "java/lang/Object;" */
	public static ClassType fromDescriptor(String classEncodedName) {
		return getOrCreateClassType(classEncodedName, encodedName -> new ClassType(encodedName));
	}
	
	/** Принимает строку с префиксом 'L', т.е. в виде "Ljava/lang/Object;" */
	public static ClassType fromTypeDescriptor(String encodedName) {
		if(!encodedName.isEmpty() && encodedName.charAt(0) == 'L')
			return fromDescriptor(encodedName.substring(1));
		
		throw new InvalidTypeNameException(encodedName);
	}
	
	
	public static ClassType fromNullableConstant(@Nullable ClassConstant clazz, ClassType defaultValue) {
		return clazz != null ? fromConstant(clazz) : defaultValue;
	}
	
	public static ClassType fromNullableDescriptor(@Nullable String classEncodedName, ClassType defaultValue) {
		return classEncodedName != null ? fromDescriptor(classEncodedName) : defaultValue;
	}
	
	
	/** Принимает строку без префикса 'L' */
	public static ClassType read(ExtendedStringInputStream in) {
		return fromDescriptor(readDescriptor(in));
	}
	
	/** Принимает строку с префиксом 'L' */
	public static ClassType readAsType(ExtendedStringInputStream in) {
		if(in.get() == 'L')
			return read(in.next());
		
		throw new InvalidClassNameException(in);
	}
	
	private static String readDescriptor(ExtendedStringInputStream in) {
		
		StringBuilder str = in.readUntil(';', '<', new StringBuilder());
		
		if(in.get() == '<')
			readSignature(in.next(), str);
		
		if(in.get() == ';')
			in.incPos();
		
		return str.toString();
	}
	
	private static void readSignature(ExtendedStringInputStream in, StringBuilder str) {
		
		str.append('<');
		
		while(true) {
			in.readUntil('<', '>', str);
			
			if(in.get() == '<') {
				readSignature(in.next(), str);
			} else {
				in.incPos();
				str.append('>');
				return;
			}
		}
	}
	
	
	private final String
			encodedName,
			classEncodedName,
			name,
			simpleName,
			fullSimpleName,
			packageName;
	
	private String nameForVariable;
	
	private final @Nullable ClassType enclosingClass;
	
	private final @Nullable GenericParameters<ReferenceType> signature;
	private final ClassType rawType;
	
	private final ClassKind kind;
	
	
	private static final Pattern
			NAME_NUM_PATTERN = Pattern.compile("\\$(\\d+)");
	
	ClassType(String classEncodedName, Class<?> clazz) {
		super(clazz);
		
		this.classEncodedName = classEncodedName;
		this.encodedName = clazz.descriptorString();
		this.name = clazz.getName().replaceAll("\\$(?!\\d)", ".");
		
		this.packageName = clazz.getPackageName();
		
		Class<?> enclosingClassInstance = clazz.getEnclosingClass();
		
		if(enclosingClassInstance != null) {
			this.enclosingClass = fromClass(enclosingClassInstance);
			
			if(clazz.isAnonymousClass()) {
				
				var matcher = NAME_NUM_PATTERN.matcher(classEncodedName);
				String nameNumber = matcher.find() ? matcher.group(1) : "0";
				
				this.simpleName = enclosingClass.simpleName + '$' + nameNumber;
				this.fullSimpleName = enclosingClass.fullSimpleName + '$' + nameNumber;
				
				this.kind = ClassKind.ANONYMOUS;
				
			} else {
				this.simpleName = clazz.getSimpleName();
				this.fullSimpleName = enclosingClass.fullSimpleName + '.' + simpleName;
				
				this.kind = ClassKind.NESTED;
			}
			
		} else {
			this.enclosingClass = null;
			this.simpleName = clazz.getSimpleName();
			this.fullSimpleName = simpleName;
			
			this.kind = classEncodedName.endsWith("/package-info") ? ClassKind.PACKAGE_INFO :
						classEncodedName.equals("module-info") ? ClassKind.MODULE_INFO : ClassKind.PLAIN;
		}
		
		this.signature = null;
		this.rawType = this;
	}
	
	
	private ClassType(String encodedName) {
		this(new ExtendedStringInputStream(encodedName));
	}
	
	
	private ClassType(ExtendedStringInputStream in) {
		
		in.mark();
		
		StringBuilder
				classEncodedNameBuilder = new StringBuilder(),
				nameBuilder = new StringBuilder();
		
		int nameStartPos = 0,
			packageEndPos = 0,
			enclosingClassNameEndPos = 0;
		
		GenericParameters<ReferenceType> signature = null;
		ClassType rawType = null;
		
		// Некоторые названия классов (такие как package-info и module-info)
		// содержат тире, и эта переменная нужна для полной проверки валидности названия
		int dashIndex = 0;
		
		Loop: for(int i = 0, ch, prevCh = 0;; i++, prevCh = ch) {
			ch = in.read();
			
			if(prevCh == '$') {
				nameBuilder.append(Character.isLetter((char)ch) ? '.' : '$');
			}
			
			switch(ch) {
				case '/' -> {
					packageEndPos = i;
					nameStartPos = i + 1;
					nameBuilder.append('.');
					classEncodedNameBuilder.append('/');
					
					// Если мы встречали тире раньше, значит, название класса невалидное
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, dashIndex);
					
					continue;
				}
				
				case '$' -> {
					enclosingClassNameEndPos = i;
					nameStartPos = i + 1;
					classEncodedNameBuilder.append('$');
					
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, dashIndex);
					
					continue;
				}
				
				case '<' -> {
					signature = parseSignature(in.prev());
					rawType = ClassType.fromDescriptor(classEncodedNameBuilder.toString());
					
					classEncodedNameBuilder.append('<');
					signature.getTypes().forEach(parameter -> classEncodedNameBuilder.append(parameter.getEncodedName()));
					classEncodedNameBuilder.append('>');
					
					
					switch(in.read()) {
						case ';', EOF_CHAR -> {
							break Loop;
						}
							
						default -> {
							throw new InvalidClassNameException(in, in.distanceToMark());
						}
					}
				}
				
				case ';', EOF_CHAR -> {
					break Loop;
				}
				
				case '-' -> {
					// Если мы встречали тире раньше, значит, название класса невалидное
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, in.distanceToMark());
					
					dashIndex = i;
				}
				
				// Недопустимые символы
				case '\b', '\t', '\n', 0xB /* '\v' */, '\f', '\r',  ' ',  '!',
					 '"',  '#',  '%',  '&',  '\'',  '(',  ')',  '*',  '+',
					 ',',  '.',  ':',  '=',  '>',   '?',  '@',  '[',  '\\',
					 ']',  '^',  '`',  '{',  '|',   '}',  '~',  0x7F /* DEL */ -> {
						throw new InvalidClassNameException(in, in.distanceToMark());
					 }
			}
			
			classEncodedNameBuilder.append((char)ch);
			nameBuilder.append((char)ch);
		}
		
		String classEncodedName = classEncodedNameBuilder.toString();
		
		this.classEncodedName = classEncodedName;
		this.encodedName = 'L' + classEncodedName + ';';
		this.name = nameBuilder.toString();
		
		String simpleName = nameBuilder.substring(nameStartPos);
		this.packageName = nameBuilder.substring(0, packageEndPos);
		
		if(enclosingClassNameEndPos != 0) { // Nested class
			boolean isAnonymous = simpleName.matches("^\\d+$");
			this.kind = isAnonymous ? ClassKind.ANONYMOUS : ClassKind.NESTED;
			this.enclosingClass = fromDescriptor(classEncodedName.substring(0, enclosingClassNameEndPos));
			
			this.fullSimpleName = enclosingClass.fullSimpleName + (isAnonymous ? '$' : '.') + simpleName;
			
			if(isAnonymous) {
				nameBuilder.setCharAt(enclosingClassNameEndPos, '$');
				simpleName = enclosingClass.getSimpleName() + '$' + simpleName;
			}
			
		} else {
			this.enclosingClass = null;
			this.fullSimpleName = simpleName;
			
			if(dashIndex != 0) {
				
				if(classEncodedName.endsWith("/package-info")) {
					this.kind = ClassKind.PACKAGE_INFO;
				} else if(classEncodedName.equals("module-info")) {
					this.kind = ClassKind.MODULE_INFO;
				} else {
					throw new InvalidClassNameException(in, dashIndex);
				}
			
			} else {
				this.kind = ClassKind.PLAIN;
			}
		}
		
		this.simpleName = simpleName;
		
		this.signature = signature;
		this.rawType = rawType == null ? this : rawType;
		
		in.unmark();
		
		ALL_CLASSES.add(this);
	}
	
	
	@Override
	public String toString() {
		return "class " + (signature == null ? name : name + signature.toString());
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		String name = classinfo.getNameForClass(this);
		assert name != null && !name.isEmpty();
		out.write(name);
		
		if(signature != null) {
			out.printObject(signature, classinfo);
		}	
	}
	
	/** @return Закодированное имя класса с префиксом 'L' и без постфиксом ';'<br>
	 * Пример: "Ljava/lang/Object;" */
	@Override
	public String getEncodedName() {
		return encodedName;
	}
	
	/** @return Закодированное имя класса без префикса 'L' и без постфикса ';'<br>
	 * Пример: "java/lang/Object" */
	@Override
	public String getClassEncodedName() {
		return classEncodedName;
	}
	
	/** @return Полное имя класса<br>
	 * Пример: для класса "java/lang/Character$Subset" вернёт "java.lang.Character.Subset" */
	@Override
	public String getName() {
		return name;
	}
	
	/** @return Имя класса без пакета и внешних классов<br>
	 * Пример: для класса "java/lang/Character$Subset" вернёт "Subset" */
	public String getSimpleName() {
		return simpleName;
	}
	
	/** @return Имя класса без пакета, но включая имена внешних класстов<br>
	 * Пример: для класса "java/lang/Character$Subset" вернёт "Character.Subset" */
	public String getFullSimpleName() {
		return fullSimpleName;
	}
	
	/** @return Имя класса пакета<br>
	 * Пример: "java.lang" */
	public String getPackageName() {
		return packageName;
	}
	
	@Override
	public final String getNameForVariable() {
		if(nameForVariable != null)
			return nameForVariable;
		
		return nameForVariable = StringUtil.toLowerCamelCase(simpleName);
	}
	
	
	public @Nullable ClassType getEnclosingClass() {
		return enclosingClass;
	}
	
	/** @return внешний класс верхнего уровня для вложенных классов,
	 * для всех остальных классов - {@literal this} */
	public ClassType getTopLevelClass() {
		return enclosingClass == null ? this : enclosingClass.getTopLevelClass();
	}
	
	public @Nullable GenericParameters<ReferenceType> getSignature() {
		return signature;
	}
	
	
	/** Возвращает не параметрезированный тип */
	public ClassType getRawType() {
		return rawType;
	}
	
	@Override
	public boolean isGenericType() {
		return signature != null;
	}
	
	
	/** package-info или module-info */
	public boolean isSpecialClassType() {
		return kind.isSpecial();
	}
	
	/** Ни package-info, ни module-info */
	public boolean isPlainClassType() {
		return kind.isPlain();
	}
	
	public boolean isNested() {
		return kind.isNested();
	}
	
	public boolean isAnonymous() {
		return kind.isAnonymous();
	}
	
	public boolean isPackageInfo() {
		return kind.isPackageInfo();
	}
	
	public boolean isModuleInfo() {
		return kind.isModuleInfo();
	}
	
	public boolean isNestmateOf(ClassType other) {
		var enclosingClass = this.enclosingClass;
		return enclosingClass == other || enclosingClass != null && enclosingClass.isNestmateOf(other);
	}
	
	
	@Override
	protected void tryLoadSuperTypes() {
		Class<?> thisClass = getClassInstance();
		
		if(thisClass != null) {
			initSuperType(thisClass);
		}
	}
	
	
	private void initSuperType(Class<?> thisClass) {
		
		if(thisClass.isInterface()) {
			setSuperType(OBJECT);
			
		} else {
			Class<?> superClass = thisClass.getSuperclass();
			
			if(superClass != null) {
				setSuperType(fromClass(superClass));
			}
		}
		
		setInterfaces(Arrays.stream(thisClass.getInterfaces()).map(ClassType::fromClass).toList());
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(this);
	}
	
	public void addImportsForSignature(ClassInfo classinfo) {
		if(signature != null)
			signature.addImports(classinfo);
	}
	
	
	@Override
	public int implicitCastStatus(Type other) {
		return other instanceof PrimitiveType primitiveType && primitiveType.getWrapperType().equals(this) ?
				CastStatus.AUTOBOXING : super.implicitCastStatus(other);
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return other.isClassType();
	}
	
	@Override
	public boolean equalsIgnoreSignature(Type other) {
		return this == other || other instanceof ClassType classType && this.equalsIgnoreSignature(classType);
	}
	
	public boolean equalsIgnoreSignature(ClassType other) {
		return this == other || name.equals(other.name);
	}
}
