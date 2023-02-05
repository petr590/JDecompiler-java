package x590.jdecompiler;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import x590.jdecompiler.attribute.AttributeNames;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.attribute.annotation.AnnotationsAttribute;
import x590.jdecompiler.attribute.signature.ClassSignatureAttribute;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.ClassFormatException;
import x590.jdecompiler.exception.IllegalClassHeaderException;
import x590.jdecompiler.exception.IllegalModifiersException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.modifiers.Modifiers;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.WhitespaceStringBuilder;
import x590.jdecompiler.util.IWhitespaceStringBuilder;
import x590.util.Pair;
import x590.util.Util;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Описывает декомпилируемый класс
 */
public class JavaClass extends JavaClassElement {
	
	private final Version version;
	private final ConstantPool pool;
	private final ClassModifiers modifiers;
	
	private final ClassType thisType, superType;
	private final @Immutable List<ClassType> interfaces;
	
	private final ClassInfo classinfo;
	
	// Суперкласс и интерфейсы, которые будут выведены в заголовок класса
	// Например, все enum классы наследуются от java.lang.Enum,
	// все аннотации реализуют интерфейс java.lang.annotation.Annotation и т.д.
	// Такие суперклассы и суперинтерфейсы не должны выводиться в заголовке класса.
	private final @Nullable Type visibleSuperType;
	private final @Immutable List<? extends Type> visibleInterfaces;
	
	private final @Immutable List<JavaField> fields, constants;
	private final @Nullable @Immutable List<JavaEnumField> enumConstants;
	private final @Immutable List<JavaMethod> methods;
	
	private final Attributes attributes;
	private final @Nullable ClassSignatureAttribute signature;
	
	
	private @Nullable Type getVisibleSuperType() {
		if(superType.equals(ClassType.OBJECT)) {
			return null;
			
		} else {
			if(modifiers.isInterface()) {
				throw new IllegalClassHeaderException("Interface cannot inherit from other class than java.lang.Object");
			}
		}
		
		if(modifiers.isEnum() && superType.equals(ClassType.ENUM)) {
			return null;
		}
		
		return signature != null ? signature.superType : superType;
	}
	
	
	private static final Predicate<ClassType> notAnnotationPredicate = interfaceType -> !interfaceType.equals(ClassType.ANNOTATION);
	
	private @Immutable List<? extends Type> getVisibleInterfaces() {
		
		var interfaces = signature != null ? signature.interfaces : this.interfaces;
		
		if(modifiers.isNotAnnotation() || interfaces.stream().allMatch(notAnnotationPredicate))
			return interfaces;
		
		return interfaces.stream().filter(notAnnotationPredicate).toList();
	}
	
	
	JavaClass(ExtendedDataInputStream in) {
		if(in.readInt() != 0xCAFEBABE)
			throw new ClassFormatException("Illegal class header");
		
		this.version = Version.read(in);
		this.pool = ConstantPool.read(in);
		var pool = this.pool;
		
		this.modifiers = ClassModifiers.read(in);
		this.thisType = ClassType.fromConstant(pool.get(in.readUnsignedShort()));
		this.superType = ClassType.fromNullableConstant(pool.get(in.readUnsignedShort()), ClassType.OBJECT);
		
		this.interfaces = in.readImmutableList(() -> pool.getClassConstant(in.readUnsignedShort()).toClassType());
		
		this.classinfo = new ClassInfo(this, version, pool, modifiers, thisType, superType, interfaces);
		
		this.fields = Collections.unmodifiableList(JavaField.readFields(in, classinfo, pool));
		this.methods = Collections.unmodifiableList(JavaMethod.readMethods(in, classinfo, pool));
		
		this.constants = fields.stream().filter(JavaField::isConstant).toList();
		this.enumConstants = modifiers.isEnum() ? fields.stream().filter(field -> field.getModifiers().isEnum()).map(field -> (JavaEnumField)field).toList() : null;
		
		this.attributes = Attributes.read(in, pool, Location.CLASS);
		classinfo.setAttributes(attributes);
		
		this.signature = attributes.get(AttributeNames.SIGNATURE);
		if(signature != null)
			signature.checkTypes(superType, interfaces);
		
		this.visibleSuperType = getVisibleSuperType();
		this.visibleInterfaces = getVisibleInterfaces();
	}
	
	public static JavaClass read(InputStream in) {
		return new JavaClass(new ExtendedDataInputStream(in));
	}
	
	public static JavaClass read(ExtendedDataInputStream in) {
		return new JavaClass(in);
	}
	
	
	public Version getVersion() {
		return version;
	}
	
	public ConstantPool getConstPool() {
		return pool;
	}
	
	@Override
	public ClassModifiers getModifiers() {
		return modifiers;
	}
	
	public ClassType getThisType() {
		return thisType;
	}
	
	public ClassType getSuperType() {
		return superType;
	}
	
	public @Immutable List<ClassType> getInterfaces() {
		return interfaces;
	}
	
	public ClassInfo getClassInfo() {
		return classinfo;
	}
	
	
	public @Immutable List<JavaField> getFields() {
		return fields;
	}
	
	public @Immutable List<JavaField> getConstants() {
		return constants;
	}
	
	public @Nullable @Immutable List<JavaEnumField> getEnumConstants() {
		return enumConstants;
	}
	
	public @Nullable @Immutable List<JavaMethod> getMethods() {
		return methods;
	}
	
	public Attributes getAttributes() {
		return attributes;
	}
	
	
	private DecompilationStage stage = DecompilationStage.DISASSEMBLED;
	
	private void checkStage(DecompilationStage requiredStage, DecompilationStage nextStage) {
		if(stage != requiredStage) {
			throw new IllegalStateException(nextStage.getExceptionMessage() + ": " +
					(stage.ordinal() < nextStage.ordinal() ? stage.next().getEarlyReason() : nextStage.getRepeatedReason()));
		}
	}
	
	private void nextStage(DecompilationStage requiredStage, DecompilationStage nextStage) {
		checkStage(requiredStage, nextStage);
		stage = nextStage;
	}
	
	
	public void decompile() {
		nextStage(DecompilationStage.DISASSEMBLED, DecompilationStage.DECOMPILED);
		
		methods.forEach(method -> method.decompile(classinfo, pool));
		if(enumConstants != null)
			enumConstants.forEach(enumConstant -> enumConstant.checkHasEnumInitializer(classinfo));
	}
	
	public void resolveImports() {
		addImports(classinfo);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		nextStage(DecompilationStage.DECOMPILED, DecompilationStage.IMPORTS_RESOLVED);
		
		classinfo.addImportIfNotNull(visibleSuperType);
		visibleInterfaces.forEach(interfaceType -> classinfo.addImport(interfaceType));
		attributes.addImports(classinfo);
		fields.forEach(field -> field.addImports(classinfo));
		methods.forEach(method -> method.addImports(classinfo));
		
		classinfo.uniqImports();
	}
	
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return super.canStringify(classinfo);
	}
	
	
	public void writeTo(StringifyOutputStream out) {
		classinfo.setOutStream(out);
		this.writeTo(out, classinfo);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		
		checkStage(DecompilationStage.IMPORTS_RESOLVED, DecompilationStage.WRITTEN);
		
		if(JDecompiler.getInstance().printClassVersion())
			out.print("/* Java version: ").print(version.toString()).println(" */");
		
		if(!thisType.getPackageName().isEmpty())
			out.print("package ").print(thisType.getPackageName()).println(';').println();
		
		classinfo.writeImports(out);
		
		writeAnnotations(out, classinfo, attributes);
		
		writeHeader(out, classinfo);
		
		// Мы можем опустить указание класса только внутри тела класса, поэтому этот метод вызывается здесь
		classinfo.enableClassOmitting();
		out.print(" {").increaseIndent();
		
		if(enumConstants != null) {
			writeEnumConstants(out, classinfo);
		}
		
		List<JavaField> stringableFields = fields.stream().filter(field -> field.canStringify(classinfo)).toList();
		List<JavaMethod> stringableMethods = methods.stream().filter(method -> method.canStringify(classinfo)).toList();
		
		
		if(!stringableFields.isEmpty())
			out.println().println();
		else if(!stringableMethods.isEmpty())
			out.println();
		
		writeFields(stringableFields, out, classinfo);
		
		stringableMethods.forEach(method -> out.println().write(method, classinfo));
		
		out.reduceIndent().print('}');
	}
	
	private void writeHeader(StringifyOutputStream out, ClassInfo classinfo) {
		
		out.printIndent().print(modifiersToString(classinfo), classinfo).print(thisType, classinfo);
		
		if(signature != null)
			out.writeIfNotNull(signature.parameters, classinfo);
		
		if(visibleSuperType != null) {
			out.print(" extends ").print(visibleSuperType, classinfo);
		}
		
		if(!visibleInterfaces.isEmpty()) {
			out.write(modifiers.isInterface() ? " extends " : " implements ");
			
			Util.forEachExcludingLast(visibleInterfaces, interfaceType -> out.write(interfaceType, classinfo), interfaceType -> out.write(", "));
		}
	}
	
	private IWhitespaceStringBuilder modifiersToString(ClassInfo classinfo) {
		IWhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
//		boolean isInnerProtected = false;
//		
//		if(thisType.kind.isNested()) {
//			InnerClassesAttribute innerClasses = attributes.get("InnerClasses");
//			if(innerClasses != null) {
//				InnerClass innerClass = innerClasses.find(thisType);
//				if(innerClass != null) {
//					int innerClassModifiers = innerClass.modifiers;
//					
//					if((innerClassModifiers & ACC_PRIVATE) != 0) str.append("private");
//					if((innerClassModifiers & ACC_PROTECTED) != 0) {
//						str.append("protected");
//						isInnerProtected = true;
//					}
//					
//					if((innerClassModifiers & ACC_STATIC) != 0) str.append("static");
//					
//					if((innerClassModifiers & ~(ACC_ACCESS_FLAGS | ACC_STATIC | ACC_SUPER)) != (modifiers & ~(ACC_ACCESS_FLAGS | ACC_SUPER)))
//						warning("modifiers of class " + thisType.getName() + " are not matching to the modifiers in InnerClasses attribute:"
//								+ Util.hex4WithPrefix(innerClassModifiers) + " " + Util.hex4WithPrefix(modifiers));
//				}
//			}
//		}
		
		switch(modifiers.and(ACC_ACCESS_FLAGS)) {
			case ACC_VISIBLE -> {}
			case ACC_PUBLIC -> {
//				if(!isInnerProtected)
					str.append("public");
			}
				
			default ->
				throw new IllegalModifiersException("in class " + thisType.getName() + ": " + modifiers.toHexWithPrefix());
		}
		
		
		if(modifiers.isStrictfp())
			str.append("strictfp");
		
		switch(modifiers.and(ACC_FINAL | ACC_ABSTRACT | ACC_INTERFACE | ACC_ANNOTATION | ACC_ENUM)) {
			case ACC_NONE ->
				str.append("class");
			case ACC_FINAL ->
				str.append("final class");
			case ACC_ABSTRACT ->
				str.append("abstract class");
			case ACC_ABSTRACT | ACC_INTERFACE ->
				str.append("interface");
			case ACC_ABSTRACT | ACC_INTERFACE | ACC_ANNOTATION ->
				str.append("@interface");
			case ACC_ENUM, ACC_FINAL | ACC_ENUM, ACC_ABSTRACT | ACC_ENUM ->
				str.append("enum");
			default ->
				throw new IllegalModifiersException("in class " + thisType.getName() + ": " + modifiers.toHexWithPrefix());
		}
		
		return str;
	}
	
	
	private void writeEnumConstants(StringifyOutputStream out, ClassInfo classinfo) {
		out.println().printIndent();
		
		Util.forEachExcludingLast(enumConstants,
				enumConstant -> enumConstant.writeNameAndInitializer(out, classinfo),
				enumConstant -> {
					if(enumConstant.hasArgumentsInEnumInitializer())
						out.println(',').printIndent();
					else
						out.write(", ");
				}
		);
		
		out.writeln(';');
	}
	
	
	private static void writeFields(List<JavaField> fields, StringifyOutputStream out, ClassInfo classinfo) {
		
		Modifiers modifiers = null;
		Type type = null; // NullPointerException никогда не возникнет для этих переменных
		Pair<AnnotationsAttribute, AnnotationsAttribute> annotationAttributes = null;
		FieldSignatureAttribute signature = null;
		boolean prevFieldWritten = false;
		
		for(JavaField field : fields) {
			
			if(prevFieldWritten) {
				
				boolean typesEquals = field.getDescriptor().getType().equals(type);
				
				if(typesEquals && field.getModifiers().equals(modifiers) &&
						field.getAnnotationAttributes().equals(annotationAttributes) && Objects.equals(field.getSignature(), signature)) {
					
					out.write(", ");
					field.writeNameAndInitializer(out, classinfo);
					continue;
					
				} else {
					out.writeln(';');
					if(!typesEquals) {
						out.writeln();
					}
				}
						
			} else {
				prevFieldWritten = true;
			}
			
			field.writeWithoutSemicolon(out, classinfo);
			
			modifiers = field.getModifiers();
			type = field.getDescriptor().getType();
			annotationAttributes = field.getAnnotationAttributes();
			signature = field.getSignature();
		}
		
		if(prevFieldWritten)
			out.writeln(';');
	}
	
	@Override
	public String toString() {
		return modifiers + " " + thisType + " extends " + superType.getName() +
				" implements " + interfaces.stream().map(Type::getName).collect(Collectors.joining(", "));
	}
}
