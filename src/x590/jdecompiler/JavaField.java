package x590.jdecompiler;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.util.List;

import x590.jdecompiler.attribute.AttributeNames;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.attribute.ConstantValueAttribute;
import x590.jdecompiler.attribute.annotation.AnnotationsAttribute;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.IllegalModifiersException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.FieldModifiers;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.util.IWhitespaceStringBuilder;
import x590.jdecompiler.util.WhitespaceStringBuilder;
import x590.util.Pair;
import x590.util.annotation.Nullable;

public class JavaField extends JavaClassElement {

	private final FieldModifiers modifiers;
	private final FieldDescriptor descriptor;
	private final Attributes attributes;
	
	private final ConstantValueAttribute constantValueAttribute;
	
	private Operation initializer;
	private final Pair<AnnotationsAttribute, AnnotationsAttribute> annotationAttributes;
	
	JavaField(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool, FieldModifiers modifiers) {
		this.modifiers = modifiers;
		this.descriptor = new FieldDescriptor(classinfo.getThisType(), in, pool);
		this.attributes = Attributes.read(in, pool, Location.FIELD);
		this.constantValueAttribute = attributes.get("ConstantValue");
		this.annotationAttributes = new Pair<>(attributes.get("RuntimeVisibleAnnotations"), attributes.get("RuntimeInvisibleAnnotations"));
	}
	
	
	static List<JavaField> readFields(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		return in.readArrayList(() -> {
			FieldModifiers modifiers = FieldModifiers.read(in);
			return modifiers.isEnum() ? new JavaEnumField(in, classinfo, pool, modifiers) : new JavaField(in, classinfo, pool, modifiers);
		});
	}
	
	
	@Override
	public FieldModifiers getModifiers() {
		return modifiers;
	}
	
	public FieldDescriptor getDescriptor() {
		return descriptor;
	}
	
	public Attributes getAttributes() {
		return attributes;
	}
	
	
	public boolean setStaticInitializer(Operation initializer) {
		
		if(modifiers.isNotStatic()) {
			throw new DecompilationException("Cannot set static initializer to the non-static field \"" + descriptor.getName() + "\"");
		}
		
		if(this.initializer == null && constantValueAttribute == null) {
			this.initializer = initializer;
			return true;
		}
		
		return false;
	}
	
	
	/** @throws IllegalArgumentException если поле не содержит атрибута ConstantValue */
	public ConstValueConstant getConstantValue() {
		if(constantValueAttribute != null)
			return constantValueAttribute.value;
		
		throw new IllegalArgumentException("Field does not contains ConstantValueAttribute");
	}
	
	
	/** @throws IllegalArgumentException если поле не содержит атрибута ConstantValue
	 * @throws IllegalArgumentException если поле содержит атрибут ConstantValue не того типа */
	@SuppressWarnings("unchecked")
	public <C extends ConstValueConstant> C getConstantValueAs(Class<C> constantClass) {
		ConstValueConstant constant = getConstantValue();
		
		if(constantClass.isInstance(constant))
			return (C)constant;
		
		throw new IllegalArgumentException("Field does not contains ConstantValueAttribute of type " + constantClass.getName());
	}
	
	
	public boolean setInstanceInitializer(Operation initializer) {
		
		if(modifiers.isStatic()) {
			throw new DecompilationException("Cannot set instance initializer to static field \"" + descriptor.getName() + "\"");
		}
		
		if(this.initializer == null && constantValueAttribute == null) {
			this.initializer = initializer;
			return true;
		}
		
		if(this.initializer.equals(initializer)) {
			return true;
			
		} else {
			this.initializer = null;
			return false;
		}
	}
	
	
	public @Nullable Operation getInitializer() {
		return initializer;
	}
	
	
	public boolean isConstant() {
		return constantValueAttribute != null;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		attributes.addImports(classinfo);
		descriptor.addImports(classinfo);
	}
	
	
	public boolean hasAnnotation() {
		return attributes.has(AttributeNames.RUNTIME_VISIBLE_ANNOTATIONS) || attributes.has(AttributeNames.RUNTIME_INVISIBLE_ANNOTATIONS);
	}
	
	public Pair<AnnotationsAttribute, AnnotationsAttribute> getAnnotationAttributes() {
		return annotationAttributes;
	}
	
	public FieldSignatureAttribute getSignature() {
		return attributes.get(AttributeNames.SIGNATURE);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		writeWithoutSemicolon(out, classinfo);
		out.writeln(';');
	}
	
	public void writeWithoutSemicolon(StringifyOutputStream out, ClassInfo classinfo) {
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(), classinfo);
		descriptor.writeType(out, classinfo, attributes);
		
		writeNameAndInitializer(out, classinfo);
	}
	
	public void writeNameAndInitializer(StringifyOutputStream out, ClassInfo classinfo) {

		out.write(descriptor.getName());
		
		if(initializer != null) {
			out.write(" = ");
			
			if(descriptor.getType().isBasicArrayType() && JDecompiler.getInstance().shortArrayInitAllowed())
				initializer.writeAsArrayInitializer(out, classinfo.getStaticInitializerStringifyContext());
			else
				initializer.writeTo(out, classinfo.getStaticInitializerStringifyContext());
			
		} else if(constantValueAttribute != null) {
			out.write(" = ");
			constantValueAttribute.writeTo(out, classinfo, descriptor.getType(), descriptor);
		}
	}
	
	private IWhitespaceStringBuilder modifiersToString() {
		IWhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
		var modifiers = this.modifiers;
		
		switch(modifiers.and(ACC_ACCESS_FLAGS)) {
			case ACC_VISIBLE   -> {}
			case ACC_PUBLIC    -> str.append("public");
			case ACC_PRIVATE   -> str.append("private");
			case ACC_PROTECTED -> str.append("protected");
			default ->
				throw new IllegalModifiersException(modifiers);
		}
		
		if(modifiers.isFinal() && modifiers.isVolatile())
			throw new IllegalModifiersException(modifiers);
		
		if(modifiers.isStatic())    str.append("static");
		if(modifiers.isFinal())     str.append("final");
		if(modifiers.isTransient()) str.append("transient");
		if(modifiers.isVolatile())  str.append("volatile");
		
		return str;
	}
	
	@Override
	public String toString() {
		return modifiers + " " + descriptor;
	}
}
