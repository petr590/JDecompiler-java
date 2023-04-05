package x590.jdecompiler.field;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.util.List;
import java.util.Objects;

import x590.jdecompiler.JavaClassElement;
import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.attribute.ConstantValueAttribute;
import x590.jdecompiler.attribute.annotation.AnnotationsAttribute;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.IllegalModifiersException;
import x590.jdecompiler.io.DisassemblingOutputStream;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.JavaMethod;
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
	
	private @Nullable FieldInfo fieldInfo;
	
	private final ConstantValueAttribute constantValueAttribute;
	private Operation initializer;
	private JavaMethod method;
	
	private final Pair<AnnotationsAttribute, AnnotationsAttribute> annotationAttributes;
	
	JavaField(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool, FieldModifiers modifiers) {
		this.modifiers = modifiers;
		this.descriptor = new FieldDescriptor(classinfo.getThisType(), in, pool);
		this.attributes = Attributes.read(in, pool, Location.FIELD);
		this.constantValueAttribute = attributes.getNullable(AttributeType.CONSTANT_VALUE);
		this.annotationAttributes = new Pair<>(attributes.getNullable(AttributeType.RUNTIME_VISIBLE_ANNOTATIONS), attributes.getNullable(AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS));
	}
	
	
	public static List<JavaField> readFields(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		return in.readArrayList(() -> {
			FieldModifiers modifiers = FieldModifiers.read(in);
			return modifiers.isEnum() ? new JavaEnumField(in, classinfo, pool, modifiers) : new JavaField(in, classinfo, pool, modifiers);
		});
	}
	
	
	public boolean isRecordComponent(ClassInfo classinfo) {
		return classinfo.isRecord() && modifiers.isNotStatic();
	}
	
	
	public boolean canStringifyAsRecordComponent(ClassInfo classinfo) {
		return super.canStringify(classinfo) && isRecordComponent(classinfo);
	}
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return super.canStringify(classinfo) && !isRecordComponent(classinfo);
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
	
	public FieldInfo getFieldInfo() {
		return fieldInfo == null ? fieldInfo = new FieldInfo(descriptor, modifiers) : fieldInfo;
	}
	
	
	public boolean setStaticInitializer(Operation initializer, DecompilationContext context) {
		
		if(modifiers.isNotStatic()) {
			throw new DecompilationException("Cannot set static initializer to the non-static field \"" + descriptor.getName() + "\"");
		}
		
		if(this.initializer == null && constantValueAttribute == null) {
			this.initializer = initializer;
			this.method = context.getMethod();
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
	
	
	public boolean setInstanceInitializer(Operation initializer, DecompilationContext context) {
		
		if(modifiers.isStatic()) {
			throw new DecompilationException("Cannot set instance initializer to static field \"" + descriptor.getName() + "\"");
		}
		
		if(constantValueAttribute != null) {
			return false;
		}
		
		if(this.initializer == null) {
			this.initializer = initializer;
			this.method = context.getMethod();
			return true;
		}
		
		return this.initializer.equals(initializer);
	}
	
	
	public @Nullable Operation getInitializer() {
		return initializer;
	}
	
	public boolean hasAnyInitializer() {
		return constantValueAttribute != null || initializer != null;
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
		return attributes.has(AttributeType.RUNTIME_VISIBLE_ANNOTATIONS) || attributes.has(AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS);
	}
	
	public Pair<AnnotationsAttribute, AnnotationsAttribute> getAnnotationAttributes() {
		return annotationAttributes;
	}
	
	public FieldSignatureAttribute getSignature() {
		return attributes.getNullable(AttributeType.FIELD_SIGNATURE);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		writeWithoutSemicolon(out, classinfo);
		out.println(';');
	}
	
	public void writeAsRecordComponent(StringifyOutputStream out, ClassInfo classinfo) {
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(recordComponentModifiersToString(), classinfo);
		descriptor.writeType(out, classinfo, attributes);
		
		writeNameAndInitializer(out, classinfo);
	}
	
	public void writeWithoutSemicolon(StringifyOutputStream out, ClassInfo classinfo) {
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(), classinfo);
		
		descriptor.writeType(out, classinfo, attributes);
		out.write(descriptor.getName());
		descriptor.getType().writeRightDefinition(out, classinfo);
	}
	
	public void writeNameAndInitializer(StringifyOutputStream out, ClassInfo classinfo) {
		
		out.write(descriptor.getName());
		descriptor.getType().writeRightDefinition(out, classinfo);
		
		if(initializer != null) {
			out.write(" = ");
			
			initializer.allowShortArrayInitializer();
			initializer.writeTo(out, method.getStringifyContext());
			
		} else if(constantValueAttribute != null) {
			out.write(" = ");
			constantValueAttribute.writeTo(out, classinfo, descriptor.getType(), descriptor);
		}
	}
	
	@Override
	public String getModifiersTarget() {
		return "field " + descriptor.toString();
	}
	
	private IWhitespaceStringBuilder modifiersToString() {
		IWhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
		var modifiers = this.modifiers;
		
		accessModifiersToString(modifiers, str);
		
		if(modifiers.isStatic()) str.append("static");
		if(modifiers.isSynthetic()) str.append("/* synthetic */");
		
		if(modifiers.isFinal() && modifiers.isVolatile())
			throw new IllegalModifiersException(this, modifiers, "field cannot be both final and volatile");
		
		if(modifiers.isFinal())     str.append("final");
		if(modifiers.isTransient()) str.append("transient");
		if(modifiers.isVolatile())  str.append("volatile");
		
		return str;
	}
	
	private IWhitespaceStringBuilder recordComponentModifiersToString() {
		IWhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
		var modifiers = this.modifiers;
		
		if(modifiers.and(ACC_ACCESS_FLAGS) != ACC_PRIVATE)
			throw new IllegalModifiersException(this, modifiers, ILLEGAL_ACCESS_MODIFIERS_MESSAGE);
		
		if(modifiers.isStatic())
			throw new IllegalModifiersException(this, modifiers, "record component cannot be static");
		
		if(modifiers.isSynthetic()) str.append("/* synthetic */");
		
		if(modifiers.isVolatile())
			throw new IllegalModifiersException(this, modifiers, "record component cannot be volatile");
		
		if(modifiers.isTransient())
			throw new IllegalModifiersException(this, modifiers, "record component cannot be transient");
		
		return str;
	}
	
	@Override
	public String toString() {
		return modifiers + " " + descriptor;
	}
	
	
	public boolean canJoinDeclaration(JavaField other) {
		return !hasAnyInitializer() && !other.hasAnyInitializer() &&
				modifiers.equals(other.modifiers) &&
				descriptor.getType().getArrayMemberIfUsingCArrays()
						.equals(other.descriptor.getType().getArrayMemberIfUsingCArrays()) &&
				Objects.equals(getSignature(), other.getSignature()) &&
				annotationAttributes.equals(other.annotationAttributes);
	}
	
	
	@Override
	public void writeDisassembled(DisassemblingOutputStream out, ClassInfo classinfo) {
		out .print(modifiersToString(), classinfo)
			.print(descriptor, classinfo);
	}
	
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		// TODO
	}
}
