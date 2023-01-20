package x590.jdecompiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.NoSuchFieldException;
import x590.jdecompiler.exception.NoSuchMethodException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.IntegerConstants;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/** Представляет собой объект, через который хранит основную информацию о классе, а так же все импорты.
 * Изначально появился из-за того, что в C++ нельзя использовать класс до его объявления, как в Java.
 * Планируется добавление интерфейса для создания экземпляра ClassInfo из java.lang.Class
 * без дополнительных танцов с бубном. */
public final class ClassInfo {
	
	private static final Map<ReferenceType, ClassInfo> INSTANCES = new HashMap<>();
	
	private final JavaClass clazz;
	
	private final Version version;
	private final ConstantPool pool;
	private final ClassModifiers modifiers;
	
	private final ClassType thisType, superType;
	private final @Immutable List<ClassType> interfaces;
	
	private Attributes attributes;
	private StringifyOutputStream out;
	
	private Map<ClassType, Integer> imports = new HashMap<>();
	private boolean importsUniqued;
	
	
	public ClassInfo(JavaClass clazz, Version version, ConstantPool pool, ClassModifiers modifiers,
			ClassType thisType, ClassType superType, @Immutable List<ClassType> interfaces) {
		
		this.clazz = clazz;
		this.version = version;
		this.pool = pool;
		this.modifiers = modifiers;
		this.thisType = thisType;
		this.superType = superType;
		this.interfaces = interfaces;
		imports.put(thisType, Integer.MAX_VALUE / 2);
		
		INSTANCES.put(thisType, this);
	}
	
	public static @Nullable ClassInfo findClassInfo(ReferenceType clazz) {
		return INSTANCES.get(clazz);
	}
	
	
	public Version getVersion() {
		return version;
	}
	
	public ConstantPool getConstPool() {
		return pool;
	}
	
	public ClassModifiers getModifiers() {
		return modifiers;
	}
	
	public ClassType getThisType() {
		return thisType;
	}
	
	public ClassType getSuperType() {
		return superType;
	}
	
	public List<ClassType> getInterfaces() {
		return interfaces;
	}
	
	public Attributes getAttributes() {
		return attributes;
	}
	
	void setAttributes(Attributes attributes) {
		if(this.attributes != null)
			throw new IllegalStateException("Attributes already setted");
		
		this.attributes = attributes;
	}
	
	void setOutStream(StringifyOutputStream out) {
		this.out = out;
	}
	
	
	public void addImport(ClassType clazz) {
		ClassType rawClass = clazz.getRawType();
		imports.put(rawClass, imports.getOrDefault(rawClass, IntegerConstants.ZERO) + 1);
		
		clazz.addImportsForSignature(this);
	}
	
	public void addImport(Type type) {
		type.addImports(this);
	}
	
	public void addImportIfNotNull(@Nullable Type type) {
		if(type != null)
			addImport(type);
	}
	
	public void uniqImports() {
		assert !importsUniqued;
		
		var groupedImports = imports.entrySet().stream().collect(Collectors.groupingBy(entry -> entry.getKey().getSimpleName()));
		groupedImports.forEach((name, list) -> {
			list.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());
			
			var iter = list.iterator();
			iter.next();
			
			while(iter.hasNext()) {
				imports.remove(iter.next().getKey());
			}
		});
		
		importsUniqued = true;
	}
	
	public boolean imported(ClassType classType) {
		return imports.containsKey(classType.getRawType());
	}
	
	public void writeImports(StringifyOutputStream out) {
		boolean written = false;
		
		for(ClassType clazz : imports.keySet()) {
			if(!clazz.getPackageName().isEmpty() && !clazz.getPackageName().equals("java.lang") && !clazz.getPackageName().equals(getThisType().getPackageName())) {
				out.printIndent().print("import ").print(clazz.getName()).println(';');
				written = true;
			}
		}
		
		if(written)
			out.println();
	}
	
	public void copyFormattingFrom(ClassInfo other) {
		imports = other.imports;
	}
	
	public void resetFormatting() {
		imports.clear();
	}
	
	
	public List<JavaField> getFields() {
		return clazz.getFields();
	}
	
	public JavaField getField(FieldDescriptor descriptor) {
		return findField(descriptor).orElseThrow(() -> new NoSuchFieldException(descriptor));
	}
	
	public Optional<JavaField> findField(FieldDescriptor descriptor) {
		return clazz.getFields().stream().filter(field -> field.getDescriptor().equals(descriptor)).findAny();
	}
	
	public boolean hasField(FieldDescriptor descriptor) {
		return clazz.getFields().stream().anyMatch(field -> field.getDescriptor().equals(descriptor));
	}
	
	
	public List<JavaField> getConstants() {
		return clazz.getConstants();
	}
	
	
	public List<JavaMethod> getMethods() {
		return clazz.getMethods();
	}
	
	public JavaMethod getMethod(MethodDescriptor descriptor) {
		return findMethod(descriptor).orElseThrow(() -> new NoSuchMethodException(descriptor));
	}
	
	public Optional<JavaMethod> findMethod(MethodDescriptor descriptor) {
		return findMethod(method -> method.getDescriptor().equals(descriptor));
	}
	
	public boolean hasMethod(MethodDescriptor descriptor) {
		return hasMethod(method -> method.getDescriptor().equals(descriptor));
	}
	
	
	public JavaMethod getMethod(Predicate<JavaMethod> predicate) {
		return findMethod(predicate).orElseThrow(() -> new NoSuchMethodException());
	}
	
	public Optional<JavaMethod> findMethod(Predicate<JavaMethod> predicate) {
		return clazz.getMethods().stream().filter(predicate).findAny();
	}
	
	public boolean hasMethod(Predicate<JavaMethod> predicate) {
		return clazz.getMethods().stream().anyMatch(predicate);
	}
	
	
	public boolean hasMethodByDescriptor(Predicate<MethodDescriptor> predicate) {
		return hasMethod(method -> predicate.test(method.getDescriptor()));
	}
	
	
	public void increaseIndent() {
		out.increaseIndent();
	}
	
	public void increaseIndent(int n) {
		out.increaseIndent(n);
	}
	
	public void reduceIndent() {
		out.reduceIndent();
	}
	
	public void reduceIndent(int n) {
		out.reduceIndent(n);
	}
	
	public String getIndent() {
		return out.getIndent();
	}
	
	
	public boolean canOmitClass(Descriptor descriptor) {
		return JDecompiler.getInstance().canOmitThisClass() && descriptor.getDeclaringClass().equals(getThisType());
	}
	
	
	private StringifyContext staticInitializerStringifyContext;
	
	public StringifyContext getStaticInitializerStringifyContext() {
		if(staticInitializerStringifyContext != null)
			return staticInitializerStringifyContext;
		
		Optional<JavaMethod> staticInitializer = clazz.getMethods().stream().filter(method -> method.getDescriptor().isStaticInitializer()).findAny();
		return staticInitializerStringifyContext = staticInitializer.isPresent() ? staticInitializer.get().getStringifyContext() : null;
	}
}
