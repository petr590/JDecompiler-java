package x590.jdecompiler.clazz;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import x590.jdecompiler.Descriptor;
import x590.jdecompiler.Importable;
import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.annotation.Annotation;
import x590.jdecompiler.attribute.signature.ClassSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.NoSuchFieldException;
import x590.jdecompiler.exception.NoSuchMethodException;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.method.JavaMethod;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.method.MethodInfo;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.RealReferenceType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.util.BooleanHolder;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;
import x590.util.function.Functions;

/**
 * Представляет собой объект, который хранит основную информацию о классе, а так же все импорты.
 * Изначально появился из-за того, что в C++ нельзя использовать класс до его объявления, в отличие от Java.
 */
public final class ClassInfo implements IClassInfo {
	
	private static final Map<ReferenceType, Optional<IClassInfo>> INSTANCES = new HashMap<>();
	
	private final JavaClass clazz;
	
	private final Version version;
	private final ConstantPool pool;
	private ClassModifiers modifiers;
	
	private final ClassType thisType;
	private final Optional<ClassType> optionalSuperType;
	private final Optional<@Immutable List<? extends ClassType>> optionalInterfaces;
	
	private @Nullable Attributes attributes;
	
	private Object2IntMap<ClassType> imports = new Object2IntOpenHashMap<>();
	private boolean importsUniqued;
	
	private Deque<ClassType> enteredClasses = new ArrayDeque<>();
	
	
	public ClassInfo(JavaClass clazz, Version version, ConstantPool pool, ClassModifiers modifiers,
			ClassType thisType, ClassType superType, @Immutable List<ClassType> interfaces) {
		
		this.clazz = clazz;
		this.version = version;
		this.pool = pool;
		this.modifiers = modifiers;
		this.thisType = thisType;
		this.optionalSuperType = Optional.of(superType);
		this.optionalInterfaces = Optional.of(interfaces);
		imports.put(thisType, Integer.MAX_VALUE / 2);
		
		INSTANCES.put(thisType, Optional.of(this));
	}
	
	public static Optional<ClassInfo> findClassInfo(@Nullable ReferenceType type) {
		return INSTANCES.get(type).filter(iclassinfo -> iclassinfo instanceof ClassInfo).map(Functions::uncheckedCast);
	}
	
	public static Optional<IClassInfo> findIClassInfo(@Nullable RealReferenceType type) {
		if(type == null) {
			return Optional.empty();
		}
		
		if(INSTANCES.containsKey(type))
			return INSTANCES.get(type);
		
		var foundClassinfo = PlainClassInfo.fromClassType(type).<IClassInfo>map(Function.identity());
		INSTANCES.put(type, foundClassinfo);
		return foundClassinfo;
	}
	
	public static Optional<IClassInfo> findIClassInfo(Optional<? extends RealReferenceType> optionalType) {
		return findIClassInfo(optionalType.orElse(null));
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
	
	void setModifiers(ClassModifiers modifiers) {
		this.modifiers = modifiers;
	}
	
	
	@Override
	public ClassType getThisType() {
		return thisType;
	}
	
	public ClassType getSuperType() {
		return optionalSuperType.get();
	}
	
	public @Immutable List<? extends ClassType> getInterfaces() {
		return optionalInterfaces.get();
	}
	
	@Override
	public Optional<ClassType> getOptionalSuperType() {
		return optionalSuperType;
	}
	
	@Override
	public Optional<@Immutable List<? extends ClassType>> getOptionalInterfaces() {
		return optionalInterfaces;
	}
	
	public Attributes getAttributes() {
		if(attributes != null)
			return attributes;
		
		throw new IllegalStateException("Attributes yet not setted");
	}
	
	void setAttributes(Attributes attributes) {
		if(this.attributes != null)
			throw new IllegalStateException("Attributes already setted");
		
		this.attributes = attributes;
	}
	
	
	@Override
	public GenericParameters<GenericDeclarationType> getSignatureParameters() {
		return clazz.getSignature().map(ClassSignatureAttribute::getParameters)
				.orElse(GenericParameters.empty());
	}
	
	
	@Override
	public Optional<Annotation> findAnnotation(ClassType type) {
		return attributes.getOrDefaultEmpty(AttributeType.RUNTIME_VISIBLE_ANNOTATIONS).findAnnotation(type);
	}
	
	
	public void addImport(ClassType clazz) {
		ClassType rawClass = clazz.getRawType();
		imports.put(rawClass, imports.getInt(rawClass) + 1);
		
		clazz.addImportsForSignature(this);
	}
	
	public void addImport(Type type) {
		type.addImports(this);
	}
	
	public void addImportIfNotNull(@Nullable Type type) {
		if(type != null)
			addImport(type);
	}
	
	public void addImportsFor(Collection<? extends Importable> importables) {
		importables.forEach(importable -> importable.addImports(this));
	}
	
	void bindEnvironmentTo(ClassInfo other) {
		if(this == other)
			return;
		
		other.imports.putAll(imports);
		imports = other.imports;
		enteredClasses = other.enteredClasses;
	}
	
	void uniqImports() {
		if(importsUniqued)
			throw new IllegalStateException("Imports already uniqued");
		
		var groupedImports = imports.object2IntEntrySet().stream()
				.collect(Collectors.groupingBy(entry -> entry.getKey().getSimpleName()));
		
		groupedImports.forEach((name, list) -> {
			
			if(JDecompiler.getConfig().canOmitSingleImport()) {
				list.removeIf(entry -> {
					
					if(entry.getIntValue() == 1) {
						imports.removeInt(entry.getKey());
						return true;
					}
					
					return false;
				});
			}
			
			
			list.sort((entry1, entry2) -> entry2.getIntValue() - entry1.getIntValue());
			
			var iter = list.iterator();
			
			if(iter.hasNext()) {
				iter.next();
				iter.forEachRemaining(entry -> imports.removeInt(entry.getKey()));
			}
		});
		
		importsUniqued = true;
	}
	
	public boolean imported(ClassType classType) {
		return imports.containsKey(classType.getRawType());
	}
	
	public void writeImports(StringifyOutputStream out) {
		writeImports(out, true);
	}
	
	public void writeImports(StringifyOutputStream out, boolean canWriteTrailingLineBreak) {
		BooleanHolder writeTrailingLineBreak = new BooleanHolder();
		
		imports.keySet().stream().sorted((class1, class2) -> class1.getName().compareTo(class2.getName()))
			.forEach(clazz -> {
				String packageName = clazz.getPackageName();
				
				if(!packageName.isEmpty() && !packageName.equals("java.lang") && !packageName.equals(thisType.getPackageName())) {
					out.printIndent().printsp("import").print(clazz.getName()).println(';');
					writeTrailingLineBreak.set(canWriteTrailingLineBreak);
				}
			});
		
		if(writeTrailingLineBreak.isTrue())
			out.println();
	}
	
	
	public @Immutable List<JavaField> getFields() {
		return clazz.getFields();
	}
	
	public @Immutable List<JavaField> getConstants() {
		return clazz.getConstants();
	}
	
	public @Nullable @Immutable List<JavaField> getRecordComponents() {
		return clazz.getRecordComponents();
	}
	
	public @Immutable List<JavaMethod> getMethods() {
		return clazz.getMethods();
	}
	
	public JavaField getField(FieldDescriptor descriptor) {
		return findField(descriptor).orElseThrow(() -> new NoSuchFieldException(descriptor));
	}
	
	public JavaMethod getMethod(MethodDescriptor descriptor) {
		return findMethod(descriptor).orElseThrow(() -> new NoSuchMethodException(descriptor));
	}
	
	public JavaMethod getMethod(Predicate<JavaMethod> predicate) {
		return findMethod(predicate).orElseThrow(NoSuchMethodException::new);
	}
	
	
	public Optional<JavaField> findField(FieldDescriptor descriptor) {
		return findField(field -> field.getDescriptor().equalsIgnoreClass(descriptor));
	}
	
	public Optional<JavaMethod> findMethod(MethodDescriptor descriptor) {
		return findMethod(method -> method.getDescriptor().equalsIgnoreClass(descriptor));
	}
	
	
	public Optional<JavaField> findField(Predicate<JavaField> predicate) {
		return clazz.getFields().stream().filter(predicate).findAny();
	}
	
	public Optional<JavaMethod> findMethod(Predicate<JavaMethod> predicate) {
		return clazz.getMethods().stream().filter(predicate).findAny();
	}
	
	
	public boolean hasField(Predicate<JavaField> predicate) {
		return clazz.getFields().stream().anyMatch(predicate);
	}
	
	public boolean hasMethod(Predicate<JavaMethod> predicate) {
		return clazz.getMethods().stream().anyMatch(predicate);
	}
	
	
	@Override
	public boolean hasFieldByDescriptor(Predicate<FieldDescriptor> predicate) {
		return hasField(field -> predicate.test(field.getDescriptor()));
	}
	
	@Override
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate) {
		return hasMethod(method -> predicate.test(method.getDescriptor()));
	}
	
	
	@Override
	public Optional<FieldInfo> findFieldInfo(FieldDescriptor descriptor) {
		return findField(descriptor).map(JavaField::getFieldInfo);
	}
	
	@Override
	public Optional<MethodInfo> findMethodInfo(MethodDescriptor descriptor) {
		return findMethod(descriptor).map(JavaMethod::getMethodInfo);
	}
	
	
	void enterClassScope(ClassType clazz) {
		enteredClasses.push(clazz);
	}
	
	void leaveClassScope(ClassType clazz) {
		if(enteredClasses.isEmpty())
			throw new IllegalStateException("Class scope has not been entered");
			
		if(!clazz.equals(enteredClasses.peek()))
			throw new IllegalArgumentException("Scope of class " + clazz.getName() +
					" has not been entered or other class has not been leaved");
		
		enteredClasses.pop();
	}
	
	public String getNameForClass(ClassType clazz) {
		if(!imported(clazz)) {
			return clazz.getName();
		}
		
		if(clazz.isAnonymous() || enteredClasses.isEmpty()) {
			return clazz.getFullSimpleName();
		}
		
		Optional<ClassType> foundClass = enteredClasses.stream()
				.filter(clazz::equalsIgnoreSignature).findAny();
		
		if(foundClass.isPresent()) {
			return foundClass.get().getSimpleName();
		}
		
		Deque<String> names = new ArrayDeque<>();
		names.addFirst(clazz.getSimpleName());
		
		for(ClassType lastEnteredClass = enteredClasses.peek(), enclosingClass = clazz;;) {
			enclosingClass = enclosingClass.getEnclosingClass();
			
			if(enclosingClass == null || enclosingClass.equalsIgnoreSignature(lastEnteredClass)) {
				break;
			}
			
			names.addFirst(enclosingClass.getSimpleName());
		}
		
		return names.stream().collect(Collectors.joining("."));
	}
	
	
	public boolean canOmitClass(Descriptor<?> descriptor) {
		return JDecompiler.getConfig().canOmitThisAndClass() &&
				enteredClasses.contains(descriptor.getDeclaringClass());
	}
	
	
	private StringifyContext staticInitializerStringifyContext;
	
	public StringifyContext getStaticInitializerStringifyContext() {
		if(staticInitializerStringifyContext != null)
			return staticInitializerStringifyContext;
		
		Optional<JavaMethod> staticInitializer = clazz.getMethods().stream().filter(method -> method.getDescriptor().isStaticInitializer()).findAny();
		return staticInitializerStringifyContext = staticInitializer.isPresent() ? staticInitializer.get().getStringifyContext() : null;
	}
	
	
	@Override
	public String toString() {
		return "ClassInfo [ " + clazz.toString() + " ]";
	}
}
