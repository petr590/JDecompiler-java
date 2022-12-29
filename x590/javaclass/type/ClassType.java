package x590.javaclass.type;

import static x590.javaclass.util.Util.EOF_CHAR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import x590.javaclass.ClassInfo;
import x590.javaclass.constpool.ClassConstant;
import x590.javaclass.exception.InvalidClassNameException;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.util.Util;

/**
 * Описывает тип java класса, самого обычного класса
 */
public final class ClassType extends ReferenceType {
	
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
			OBJECT        = new ClassType("java/lang/Object"),
			STRING        = new ClassType("java/lang/String"),
			CLASS         = new ClassType("java/lang/Class"),
			ENUM          = new ClassType("java/lang/Enum"),
			ANNOTATION    = new ClassType("java/lang/annotation/Annotation"),
			THROWABLE     = new ClassType("java/lang/Throwable"),
			EXCEPTION     = new ClassType("java/lang/Exception"),
			METHOD_TYPE   = new ClassType("java/lang/invoke/MethodType"),
			METHOD_HANDLE = new ClassType("java/lang/invoke/MethodHandle"),
			
			BYTE      = new ClassType("java/lang/Byte"),
			SHORT     = new ClassType("java/lang/Short"),
			CHARACTER = new ClassType("java/lang/Character"),
			INTEGER   = new ClassType("java/lang/Integer"),
			LONG      = new ClassType("java/lang/Long"),
			FLOAT     = new ClassType("java/lang/Float"),
			DOUBLE    = new ClassType("java/lang/Double"),
			BOOLEAN   = new ClassType("java/lang/Boolean"),
			VOID      = new ClassType("java/lang/Void");
	
	
	protected final String
			classEncodedName,
			simpleName,
			fullSimpleName,
			packageName;
	
	protected String nameForVariable;
	
	public final List<ReferenceType> parameters;
	
	public final ClassType enclosingClass;
	public final ClassKind kind;
	
	
	public static ClassType valueOf(ClassConstant clazz) {
		return valueOf(clazz.getNameConstant().getString());
	}
	
	/** Принимает строку без префикса 'L', т.е. в виде "java/lang/Object;" */
	public static ClassType valueOf(String classEncodedName) {
		if(CLASS_TYPES.containsKey(classEncodedName))
			return CLASS_TYPES.get(classEncodedName);
		
		return new ClassType(classEncodedName);
	}
	
	
	public static ClassType valueOfOrDefault(@Nullable String classEncodedName, ClassType defaultValue) {
		return classEncodedName != null ? valueOf(classEncodedName) : defaultValue;
	}
	
	public static ClassType valueOfOrDefault(@Nullable ClassConstant clazz, ClassType defaultValue) {
		return clazz != null ? valueOf(clazz) : defaultValue;
	}
	
	
	/** Принимает строку с префиксом 'L', т.е. в виде "Ljava/lang/Object;" */
	public static ClassType valueOfEncoded(String encodedName) {
		if(!encodedName.isEmpty() && encodedName.charAt(0) == 'L')
			return valueOf(encodedName.substring(1));
		
		throw new InvalidClassNameException(encodedName);
	}
	
	
	private void cacheIfNotCached() {
		if(!CLASS_TYPES.containsKey(classEncodedName))
			CLASS_TYPES.put(classEncodedName, this);
	}
	
	
	private ClassType(String encodedName) {
		this(new ExtendedStringReader(encodedName));
	}
	
	public ClassType(ExtendedStringReader in) {
		
		in.mark();
		
		StringBuilder
				encodedNameBuilder = new StringBuilder(),
				nameBuilder = new StringBuilder();
		
		this.parameters = new ArrayList<>();
		
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
					encodedNameBuilder.append('/');
					
					// Если мы встречали тире раньше, значит, название класса невалидное
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, dashIndex);
					
					continue;
				}
					
				case '$' -> {
					enclosingClassNameEndPos = i;
					nameStartPos = i + 1;
					nameBuilder.append('.');
					encodedNameBuilder.append('$');
					
					if(dashIndex != 0)
						throw new InvalidClassNameException(in, dashIndex);
					
					continue;
				}
					
				case '<' -> {
					parameters.addAll(parseParameters(in.prev()));
					
					switch(in.read()) {
						case ';', EOF_CHAR -> {
							break Loop;
						}
							
						default -> {
							throw new InvalidClassNameException(in, i);
						}
					}
				}
				
				case ';', EOF_CHAR -> {
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
					 ',',  '.',  ':',  '=',  '?',   '@',  '[',  '\\',
					 ']',  '^',  '`',  '{',  '|',   '}',  '~',  0x7F /* DEL */ -> {
						throw new InvalidClassNameException(in, i);
					 }
			}
			
			encodedNameBuilder.append((char)ch);
			nameBuilder.append((char)ch);
		}
		
		String encodedName = encodedNameBuilder.toString();
		
		this.encodedName = 'L' + encodedName + ';';
		this.classEncodedName = encodedName;
		this.name = nameBuilder.toString();
		
		String simpleName = nameBuilder.substring(nameStartPos);
		this.packageName = nameBuilder.substring(0, packageEndPos);
		
		if(enclosingClassNameEndPos != 0) { // Nested class
			boolean isAnonymous = simpleName.matches("^\\d+$");
			this.kind = isAnonymous ? ClassKind.ANONYMOUS : ClassKind.NESTED;
			this.enclosingClass = new ClassType(encodedName.substring(0, enclosingClassNameEndPos));
			
			this.fullSimpleName = enclosingClass.fullSimpleName + (isAnonymous ? '$' : '.') + simpleName;
			
			if(isAnonymous) {
				nameBuilder.setCharAt(enclosingClassNameEndPos, '$');
				simpleName = enclosingClass.getSimpleName() + '$' + simpleName;
			}
			
		} else {
			this.enclosingClass = null;
			this.fullSimpleName = simpleName;
			
			if(dashIndex != 0) {
				
				if(encodedName.endsWith("/package-info"))
					this.kind = ClassKind.PACKAGE_INFO;
				else if(encodedName.endsWith("/module-info"))
					this.kind = ClassKind.MODULE_INFO;
				else
					throw new InvalidClassNameException(in, dashIndex);
			
			} else {
				this.kind = ClassKind.PLAIN;
			}
		}
		
		this.simpleName = simpleName;
		
		in.unmark();
		
		
		this.cacheIfNotCached();
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return getName(classinfo);
	}
	
	@Override
	public String toString() {
		return "class " + (parameters.isEmpty() ? name : name +
				"<" + parameters.stream().map(type -> type.toString()).collect(Collectors.joining(", ")) + '>');
//				(superType != null ? " extends " + superType.getName() : "") +
//				(interfaces != null ? " implements " + interfaces.stream().map(ReferenceType::getName).collect(Collectors.joining(", ")) : "");
	}
	
	@Override
	public String getName(ClassInfo classinfo) {
		return kind.isAnonymous() ? fullSimpleName : (classinfo.imported(this) ? simpleName : name) + (parameters.isEmpty() ? "" :
			'<' + parameters.stream().map(type -> type.toString(classinfo)).collect(Collectors.joining(", ")) + ">");
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
	
	public boolean isAnonymous() {
		return kind.isAnonymous();
	}
	
	@Override
	public final boolean isBasicClassType() {
		return true;
	}
	
	
	private boolean triedLoadClass;
	
	@Override
	protected void tryLoadSuperType() {
		if(!triedLoadClass) {
			
			try {
				Class<?> thisClass = Class.forName(name);
				Class<?> superClass = thisClass.getSuperclass();
				
				if(superClass != null) {
					superType = valueOfEncoded(superClass.descriptorString());
					interfaces = Arrays.stream(thisClass.getInterfaces())
							.<ReferenceType>map(interfaceClass -> ClassType.valueOfEncoded(interfaceClass.descriptorString())).toList();
				}
				
			} catch(ClassNotFoundException ex) {
				
				System.out.println("Class \"" + name + "\" not found among java classes");
				
				triedLoadClass = true;
			}
		}
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(this);
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
}
