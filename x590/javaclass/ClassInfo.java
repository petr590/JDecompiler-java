package x590.javaclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.NoSuchFieldException;
import x590.javaclass.exception.NoSuchMethodException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class ClassInfo {
	
	public final JavaClass clazz;
	
	public final Version version;
	public final ConstantPool pool;
	public final Modifiers modifiers;
	
	public final ClassType thisType, superType;
	public final List<ClassType> interfaces;
	
	private Attributes attributes;
	
	private Map<ClassType, Integer> imports = new HashMap<>();
	private boolean importsUniqued;
	
	
	public ClassInfo(JavaClass clazz, Version version, ConstantPool pool, Modifiers modifiers,
			ClassType thisType, ClassType superType, List<ClassType> interfaces) {
		
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
		imports.put(clazz, imports.getOrDefault(clazz, ZERO) + 1);
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
		groupedImports.entrySet().removeIf(entry -> entry.getValue().size() <= 1);
		
		importsUniqued = true;
	}
	
	public boolean imported(ClassType classType) {
		return imports.containsKey(classType);
	}
	
	public void writeImports(StringifyOutputStream out) {
		boolean written = false;
		
		for(ClassType clazz : imports.keySet()) {
			if(!clazz.getPackageName().equals("java.lang") && !clazz.getPackageName().equals(thisType.getPackageName())) {
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
		return clazz.fields;
	}
	
	public JavaField getField(FieldDescriptor descriptor) {
		return findField(descriptor).orElseThrow(() -> new NoSuchFieldException(descriptor));
	}
	
	public Optional<JavaField> findField(FieldDescriptor descriptor) {
		return clazz.fields.stream().filter(field -> field.descriptor.equals(descriptor)).findAny();
	}
	
	public boolean hasField(FieldDescriptor descriptor) {
		return clazz.fields.stream().anyMatch(field -> field.descriptor.equals(descriptor));
	}
	
	
	public List<JavaField> getConstants() {
		return clazz.constants;
	}
	
	
	public List<JavaMethod> getMethods() {
		return clazz.methods;
	}
	
	public JavaMethod getMethod(MethodDescriptor descriptor) {
		return findMethod(descriptor).orElseThrow(() -> new NoSuchMethodException(descriptor));
	}
	
	public Optional<JavaMethod> findMethod(MethodDescriptor descriptor) {
		return clazz.methods.stream().filter(method -> method.descriptor.equals(descriptor)).findAny();
	}
	
	public boolean hasMethod(MethodDescriptor descriptor) {
		return clazz.methods.stream().anyMatch(method -> method.descriptor.equals(descriptor));
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
	
	
	private StringifyContext staticInitializerStringifyContext;
	
	public StringifyContext getStaticInitializerStringifyContext() {
		if(staticInitializerStringifyContext != null)
			return staticInitializerStringifyContext;
		
		Optional<JavaMethod> staticInitializer = clazz.methods.stream().filter(method -> method.descriptor.isStaticInitializer()).findAny();
		return staticInitializerStringifyContext = staticInitializer.isPresent() ? staticInitializer.get().getStringifyContext() : null;
	}
}