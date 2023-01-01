package x590.javaclass.type;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import x590.javaclass.ClassInfo;
import x590.javaclass.constpool.ClassConstant;
import x590.javaclass.exception.InvalidClassNameException;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.util.Util;

/**
 * Описывает тип java класса, самого обычного класса
 */
public class ClassType extends ReferenceType {
	
	/**
	 * Определяет, какой это класс: обычный, вложенный, анонимный, package-info, module-info
	 */
	public enum ClassKind {
		PLAIN("plain", false),
		NESTED("nested", false),
		ANONYMOUS("anonymous", false),
		PACKAGE_INFO("package-info", true),
		MODULE_INFO("module-info", true);
		
		public final String name;
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
	
	
	public static final ClassType
			OBJECT        = new ClassType("java/lang/Object", Object.class),
			STRING        = new ClassType("java/lang/String", String.class),
			CLASS         = new ClassType("java/lang/Class", Class.class),
			ENUM          = new ClassType("java/lang/Enum", Enum.class),
			ANNOTATION    = new ClassType("java/lang/annotation/Annotation", Annotation.class),
			THROWABLE     = new ClassType("java/lang/Throwable", Throwable.class),
			EXCEPTION     = new ClassType("java/lang/Exception", Exception.class),
			METHOD_TYPE   = new ClassType("java/lang/invoke/MethodType", MethodType.class),
			METHOD_HANDLE = new ClassType("java/lang/invoke/MethodHandle", MethodHandle.class),
			
			BYTE      = new WrapperClassType("java/lang/Byte", Byte.class, PrimitiveType.BYTE),
			SHORT     = new WrapperClassType("java/lang/Short", Short.class, PrimitiveType.SHORT),
			CHARACTER = new WrapperClassType("java/lang/Character", Character.class, PrimitiveType.CHAR),
			INTEGER   = new WrapperClassType("java/lang/Integer", Integer.class, PrimitiveType.INT),
			LONG      = new WrapperClassType("java/lang/Long", Long.class, PrimitiveType.LONG),
			FLOAT     = new WrapperClassType("java/lang/Float", Float.class, PrimitiveType.FLOAT),
			DOUBLE    = new WrapperClassType("java/lang/Double", Double.class, PrimitiveType.DOUBLE),
			BOOLEAN   = new WrapperClassType("java/lang/Boolean", Boolean.class, PrimitiveType.BOOLEAN),
			VOID      = new WrapperClassType("java/lang/Void", Void.class, PrimitiveType.VOID);
	
	
	public static ClassType fromConstant(ClassConstant clazz) {
		return fromDescriptor(clazz.getNameConstant().getString());
	}
	
	public static ClassType fromNullableConstant(@Nullable ClassConstant clazz, ClassType defaultValue) {
		return clazz != null ? fromConstant(clazz) : defaultValue;
	}
	
	
	/** Принимает строку без префикса 'L', т.е. в виде "java/lang/Object;" */
	public static ClassType fromDescriptor(String classEncodedName) {
		if(CLASS_TYPES.containsKey(classEncodedName))
			return CLASS_TYPES.get(classEncodedName);
		
		return new ClassType(classEncodedName);
	}
	
	public static ClassType fromNullableDescriptor(@Nullable String classEncodedName, ClassType defaultValue) {
		return classEncodedName != null ? fromDescriptor(classEncodedName) : defaultValue;
	}
	
	/** Принимает строку с префиксом 'L', т.е. в виде "Ljava/lang/Object;" */
	public static ClassType fromTypeDescriptor(String encodedName) {
		if(!encodedName.isEmpty() && encodedName.charAt(0) == 'L')
			return fromDescriptor(encodedName.substring(1));
		
		throw new InvalidClassNameException(encodedName);
	}
	
	
	/** Принимает строку без префикса 'L' */
	public static ClassType read(ExtendedStringReader in) {
		return fromDescriptor(readDescriptor(in));
	}
	
	/** Принимает строку с префиксом 'L' */
	public static ClassType readAsType(ExtendedStringReader in) {
		if(in.get() == 'L')
			return read(in.next());
		
		throw new InvalidClassNameException(in);
	}
	
	public static String readDescriptor(ExtendedStringReader in) {
		
		StringBuilder str = in.readUntil(';', '<', new StringBuilder());
		
		if(in.get() == '<')
			readSignature(in.next(), str);
		
		if(in.get() == ';')
			in.incPos();
		
		return str.toString();
	}
	
	public static void readSignature(ExtendedStringReader in, StringBuilder str) {
		
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
	
	
	protected final String
			classEncodedName,
			simpleName,
			fullSimpleName,
			packageName;
	
	protected String nameForVariable;
	
	public final ClassType enclosingClass;
	
	protected @Nullable GenericParameters<ReferenceType> signature;
	private ClassType rawType;
	
	public final ClassKind kind;
	
	private boolean triedLoadClass;
	
	
	private void cacheIfNotCached() {
		if(!CLASS_TYPES.containsKey(classEncodedName))
			CLASS_TYPES.put(classEncodedName, this);
	}
	
	
	protected ClassType(String encodedName, Class<?> thisClass) {
		this(encodedName);
		initSuperType(thisClass);
		triedLoadClass = true;
	}
	
	protected ClassType(String encodedName) {
		this(new ExtendedStringReader(encodedName));
	}
	
	protected ClassType(ExtendedStringReader in) {
		
		in.mark();
		
		StringBuilder
				classEncodedNameBuilder = new StringBuilder(),
				nameBuilder = new StringBuilder();
		
		int nameStartPos = 0,
			packageEndPos = 0,
			enclosingClassNameEndPos = 0;
		
		// Некоторые названия классов (такие как package-info и module-info)
		// содержат тире, и эта переменная нужна для полной проверки валидности названия
		int dashIndex = 0;
		
		Loop: for(int i = 0;; i++) {
			int ch = in.read();
			
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
					nameBuilder.append('.');
					classEncodedNameBuilder.append('$');
					
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, dashIndex);
					
					continue;
				}
					
				case '<' -> {
					signature = parseSignature(in.prev());
					rawType = ClassType.fromDescriptor(classEncodedNameBuilder.toString());
					
					classEncodedNameBuilder.append('<');
					signature.types.forEach(parameter -> classEncodedNameBuilder.append(parameter.getEncodedName()));
					classEncodedNameBuilder.append('>');
					
					
					switch(in.read()) {
						case ';', ExtendedStringReader.EOF_CHAR -> {
							break Loop;
						}
							
						default -> {
							throw new InvalidClassNameException(in, i);
						}
					}
				}
				
				case ';', ExtendedStringReader.EOF_CHAR -> {
					break Loop;
				}
				
				case '-' -> {
					// Если мы встречали тире раньше, значит, название класса невалидное
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, i);
					
					dashIndex = i;
				}
				
				// Недопустимые символы
				case '\b', '\t', '\n', 0xB /* '\v' */, '\f', '\r',  ' ',  '!',
					 '"',  '#',  '%',  '&',  '\'',  '(',  ')',  '*',  '+',
					 ',',  '.',  ':',  '=',  '>',   '?',  '@',  '[',  '\\',
					 ']',  '^',  '`',  '{',  '|',   '}',  '~',  0x7F /* DEL */ -> {
						throw new InvalidClassNameException(in, i);
					 }
			}
			
			classEncodedNameBuilder.append((char)ch);
			nameBuilder.append((char)ch);
		}
		
		String classEncodedName = classEncodedNameBuilder.toString();
		
		this.encodedName = "L" + classEncodedName + ";";
		this.classEncodedName = classEncodedName;
		this.name = nameBuilder.toString();
		
		String simpleName = nameBuilder.substring(nameStartPos);
		this.packageName = nameBuilder.substring(0, packageEndPos);
		
		if(enclosingClassNameEndPos != 0) { // Nested class
			boolean isAnonymous = simpleName.matches("^\\d+$");
			this.kind = isAnonymous ? ClassKind.ANONYMOUS : ClassKind.NESTED;
			this.enclosingClass = new ClassType(classEncodedName.substring(0, enclosingClassNameEndPos));
			
			this.fullSimpleName = enclosingClass.fullSimpleName + (isAnonymous ? '$' : '.') + simpleName;
			
			if(isAnonymous) {
				nameBuilder.setCharAt(enclosingClassNameEndPos, '$');
				simpleName = enclosingClass.getSimpleName() + '$' + simpleName;
			}
			
		} else {
			this.enclosingClass = null;
			this.fullSimpleName = simpleName;
			
			if(dashIndex != 0) {
				
				if(classEncodedName.endsWith("/package-info"))
					this.kind = ClassKind.PACKAGE_INFO;
				else if(classEncodedName.endsWith("/module-info"))
					this.kind = ClassKind.MODULE_INFO;
				else
					throw new InvalidClassNameException(in, dashIndex);
			
			} else {
				this.kind = ClassKind.PLAIN;
			}
		}
		
		this.simpleName = simpleName;
		
		if(rawType == null)
			rawType = this;
		
		in.unmark();
		
		
		this.cacheIfNotCached();
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return getName(classinfo);
	}
	
	@Override
	public String toString() {
		return "class " + (signature == null ? name : name + signature.toString());
//				(superType != null ? " extends " + superType.getName() : "") +
//				(interfaces != null ? " implements " + interfaces.stream().map(ReferenceType::getName).collect(Collectors.joining(", ")) : "");
	}
	
	@Override
	public String getName(ClassInfo classinfo) {
		return kind.isAnonymous() ? fullSimpleName : (classinfo.imported(this) ? simpleName : name) + (signature == null ? "" :
			signature.toString(classinfo));
	}
	
	@Override
	public final String getNameForVariable() {
		if(nameForVariable != null)
			return nameForVariable;
		
		return nameForVariable = Util.toLowerCamelCase(simpleName);
	}
	
	@Override
	public String getClassEncodedName() {
		return classEncodedName;
	}
	
	public String getSimpleName() {
		return simpleName;
	}
	
	public String getFullSimpleName() {
		return fullSimpleName;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	
	/** Возвращает не параметрезированный тип */
	public ClassType getRawType() {
		return rawType;
	}
	
	
	public boolean isAnonymous() {
		return kind.isAnonymous();
	}
	
	@Override
	public final boolean isBasicClassType() {
		return true;
	}
	
	
	@Override
	protected void tryLoadSuperType() {
		if(!triedLoadClass) {
			
			triedLoadClass = true;
			
			try {
				initSuperType(Class.forName(name));
			} catch(ClassNotFoundException ex) {
				System.out.println("Class \"" + name + "\" not found among java classes");
			}
		}
	}
	
	
	protected void initSuperType(Class<?> thisClass) {
		Class<?> superClass = thisClass.getSuperclass();
		
		if(superClass != null) {
			superType = fromTypeDescriptor(superClass.descriptorString());
		} else if(thisClass.isInterface()) {
			superType = OBJECT;
		}
		
		interfaces = Arrays.stream(thisClass.getInterfaces())
				.<ReferenceType>map(interfaceClass -> ClassType.fromTypeDescriptor(interfaceClass.descriptorString())).toList();
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
		return other.isPrimitive() && ((PrimitiveType)other).getWrapperType().equals(this) ?
				CastStatus.AUTOBOXING : super.implicitCastStatus(other);
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return other.isBasicClassType();
	}
	
	/** Сравнивает типы без учёта сигнатуры */
	public boolean baseEquals(ClassType other) {
		return this == other || this.name.equals(other.name);
	}
}
