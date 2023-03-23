package x590.jdecompiler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
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
import x590.util.BooleanHolder;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Представляет собой объект, который хранит основную информацию о классе, а так же все импорты.
 * Изначально появился из-за того, что в C++ нельзя использовать класс до его объявления, в отличие от Java.
 */
public final class ClassInfo implements IClassInfo {
	
	private static final Map<ReferenceType, IClassInfo> INSTANCES = new HashMap<>();
	
	private final JavaClass clazz;
	
	private final Version version;
	private final ConstantPool pool;
	private final ClassModifiers modifiers;
	
	private final ClassType thisType, superType;
	private final @Immutable List<ClassType> interfaces;
	
	private Attributes attributes;
	
	private Object2IntMap<ClassType> imports = new Object2IntOpenHashMap<>();
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
	
	public static @Nullable IClassInfo findClassInfo(@Nullable ReferenceType thisType) {
		if(thisType == null) {
			return null;
		}
		
		if(INSTANCES.containsKey(thisType))
			return INSTANCES.get(thisType);
		
		PlainClassInfo classinfo = PlainClassInfo.fromClassType(thisType);
		INSTANCES.put(thisType, classinfo);
		return classinfo;
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
	
	@Override
	public ClassType getThisType() {
		return thisType;
	}
	
	@Override
	public ClassType getSuperType() {
		return superType;
	}
	
	@Override
	public @Immutable List<ClassType> getInterfaces() {
		return interfaces;
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
	
	void bindImportsTo(ClassInfo other) {
		imports = other.imports;
	}
	
	void uniqImports() {
		if(importsUniqued)
			throw new IllegalStateException("Imports already uniqued");
		
		var groupedImports = imports.object2IntEntrySet().stream().collect(Collectors.groupingBy(entry -> entry.getKey().getSimpleName()));
		
		groupedImports.forEach((name, list) -> {
			
			if(JDecompiler.getInstance().canOmitSingleImport()) {
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
		return clazz.getFields().stream().filter(field -> field.getDescriptor().equals(descriptor)).findAny();
	}
	
	public Optional<JavaMethod> findMethod(MethodDescriptor descriptor) {
		return findMethod(method -> method.getDescriptor().equals(descriptor));
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

	
	private boolean canOmitClass;
	
	void enableClassOmitting() {
		canOmitClass = JDecompiler.getInstance().canOmitThisAndClass();
	}
	
	public boolean canOmitClass(Descriptor descriptor) {
		return canOmitClass && descriptor.getDeclaringClass().equals(thisType);
	}
	
	
	private StringifyContext staticInitializerStringifyContext;
	
	public StringifyContext getStaticInitializerStringifyContext() {
		if(staticInitializerStringifyContext != null)
			return staticInitializerStringifyContext;
		
		Optional<JavaMethod> staticInitializer = clazz.getMethods().stream().filter(method -> method.getDescriptor().isStaticInitializer()).findAny();
		return staticInitializerStringifyContext = staticInitializer.isPresent() ? staticInitializer.get().getStringifyContext() : null;
	}
}
