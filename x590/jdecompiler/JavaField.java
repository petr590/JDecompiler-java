package x590.jdecompiler;

import static x590.jdecompiler.modifiers.Modifiers.*;

import java.util.List;

import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.attribute.ConstantValueAttribute;
import x590.jdecompiler.attribute.annotation.AnnotationsAttribute;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.IllegalModifiersException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.FieldModifiers;
import x590.jdecompiler.modifiers.Modifiers;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.util.IWhitespaceStringBuilder;
import x590.jdecompiler.util.WhitespaceStringBuilder;
import x590.util.Pair;
import x590.util.annotation.Nullable;

public class JavaField extends JavaClassElement {

	public final FieldModifiers modifiers;
	public final FieldDescriptor descriptor;
	public final Attributes attributes;
	
	public final ConstantValueAttribute constantValueAttribute;
	
	protected Operation initializer;
	protected final Pair<AnnotationsAttribute, AnnotationsAttribute> annotationAttributes;
	
	protected JavaField(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool, FieldModifiers modifiers) {
		this.modifiers = modifiers;
		this.descriptor = new FieldDescriptor(classinfo.thisType, in, pool);
		this.attributes = new Attributes(in, pool, Location.FIELD);
		this.constantValueAttribute = attributes.get("ConstantValue");
		this.annotationAttributes = new Pair<>(attributes.get("RuntimeVisibleAnnotations"), attributes.get("RuntimeInvisibleAnnotations"));
	}
	
	
	protected static List<JavaField> readFields(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		return in.readArrayList(() -> {
			FieldModifiers modifiers = new FieldModifiers(in.readUnsignedShort());
			return modifiers.isEnum() ? new JavaEnumField(in, classinfo, pool, modifiers) : new JavaField(in, classinfo, pool, modifiers);
		});
	}
	
	
	@Override
	public Modifiers getModifiers() {
		return modifiers;
	}
	
	public boolean setInitializer(Operation initializer) {
		if(this.initializer == null && constantValueAttribute == null) {
			this.initializer = initializer;
			return true;
		}
		
		return false;
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
		return attributes.has("RuntimeVisibleAnnotations") || attributes.has("RuntimeInvisibleAnnotations");
	}
	
	public Pair<AnnotationsAttribute, AnnotationsAttribute> getAnnotationAttributes() {
		return annotationAttributes;
	}
	
	public FieldSignatureAttribute getSignature() {
		return attributes.get("Signature");
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

		out.write(descriptor.name);
		
		if(initializer != null) {
			out.write(" = ");
			
			if(descriptor.type.isBasicArrayType() && JDecompiler.getInstance().shortArrayInitAllowed())
				initializer.writeAsArrayInitializer(out, classinfo.getStaticInitializerStringifyContext());
			else
				initializer.writeTo(out, classinfo.getStaticInitializerStringifyContext());
			
		} else if(constantValueAttribute != null) {
			out.write(" = ");
			constantValueAttribute.writeAs(out, classinfo, descriptor.type);
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
}
