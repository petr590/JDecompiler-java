package x590.jdecompiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import x590.jdecompiler.type.Type;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

// @Immutable
public class ClassInfo {
	
	public final JavaClass clazz;
	
	public final Version version;
	public final ConstantPool pool;
	public final ClassModifiers modifiers;
	
	public final ClassType thisType, superType;
	public final @Immutable List<ClassType> interfaces;
	
	private Attributes attributes;
	
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
	}
	
	private static final Integer ZERO = 0;
	
	public void addImport(ClassType clazz) {
		ClassType rawClass = clazz.getRawType();
		imports.put(rawClass, imports.getOrDefault(rawClass, ZERO) + 1);
		
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
			if(!clazz.getPackageName().isEmpty() && !clazz.getPackageName().equals("java.lang") && !clazz.getPackageName().equals(thisType.getPackageName())) {
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
		return clazz.getFields().stream().filter(field -> field.descriptor.equals(descriptor)).findAny();
	}
	
	public boolean hasField(FieldDescriptor descriptor) {
		return clazz.getFields().stream().anyMatch(field -> field.descriptor.equals(descriptor));
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
		return clazz.getMethods().stream().filter(method -> method.descriptor.equals(descriptor)).findAny();
	}
	
	public boolean hasMethod(MethodDescriptor descriptor) {
		return clazz.getMethods().stream().anyMatch(method -> method.descriptor.equals(descriptor));
	}
	
	
	protected void setAttributes(Attributes attributes) {
		if(this.attributes != null)
			throw new IllegalStateException("Attributes already setted");
		
		this.attributes = attributes;
	}
	
	public Attributes getAttributes() {
		return attributes;
	}
	
	private StringifyOutputStream out;
	
	public void setOutStream(StringifyOutputStream out) {
		this.out = out;
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
		return JDecompiler.getInstance().canOmitThisClass() && descriptor.clazz.equals(thisType);
	}
	
	
	private StringifyContext staticInitializerStringifyContext;
	
	public StringifyContext getStaticInitializerStringifyContext() {
		if(staticInitializerStringifyContext != null)
			return staticInitializerStringifyContext;
		
		Optional<JavaMethod> staticInitializer = clazz.getMethods().stream().filter(method -> method.descriptor.isStaticInitializer()).findAny();
		return staticInitializerStringifyContext = staticInitializer.isPresent() ? staticInitializer.get().getStringifyContext() : null;
	}
}
