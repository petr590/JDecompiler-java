package x590.javaclass;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.ConstantValueAttribute;
import x590.javaclass.attribute.Attributes.Location;
import x590.javaclass.attribute.annotation.AnnotationsAttribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.IllegalModifiersException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.util.IWhitespaceStringBuilder;
import x590.javaclass.util.WhitespaceStringBuilder;
import x590.jdecompiler.JDecompiler;
import x590.util.Pair;
import x590.util.lazyloading.LazyLoadingValue;

import static x590.javaclass.Modifiers.*;

public class JavaField extends JavaClassMember {
	
	public final FieldDescriptor descriptor;
	public final Attributes attributes;
	
	public final ConstantValueAttribute constantValueAttribute;
//	public final Type genericType; // TODO
	
	private Operation initializer;
	private final LazyLoadingValue<Pair<AnnotationsAttribute, AnnotationsAttribute>> annotationAttributes;
	
	protected JavaField(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		this(new Modifiers(in.readUnsignedShort()), new FieldDescriptor(classinfo.thisType, in, pool), new Attributes(in, pool, Location.FIELD));
	}
	
	protected JavaField(Modifiers modifiers, FieldDescriptor descriptor, Attributes attributes) {
		super(modifiers);
		this.descriptor = descriptor;
		this.attributes = attributes;
		this.constantValueAttribute = attributes.get("ConstantValue");
		this.annotationAttributes = new LazyLoadingValue<>(() -> new Pair<>(attributes.get("RuntimeVisibleAnnotations"), attributes.get("RuntimeInvisibleAnnotations")));
	}
	
	
	protected static List<JavaField> readFields(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		int length = in.readUnsignedShort();
		List<JavaField> fields = new ArrayList<>(length);
		
		for(int i = 0; i < length; i++) {
			fields.add(new JavaField(in, classinfo, pool));
		}
		
		return fields;
	}
	
	
	public boolean setInitializer(Operation initializer) {
		if(this.initializer == null && constantValueAttribute == null) {
			this.initializer = initializer;
			return true;
		}
		
		return false;
	}
	
	
	public boolean isConstant() {
		return constantValueAttribute != null;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		attributes.addImports(classinfo);
		descriptor.addImports(classinfo);
	}
	
	
	@Override
	public boolean canStringify(ClassInfo classinfo) {
		return super.canStringify(classinfo);
	}
	
	public boolean hasAnnotation() {
		return attributes.has("RuntimeVisibleAnnotations") || attributes.has("RuntimeInvisibleAnnotations");
	}
	
	public Pair<AnnotationsAttribute, AnnotationsAttribute> getAnnotationAttributes() {
		return annotationAttributes.get();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		writeWithoutSemicolon(out, classinfo);
		out.writeln(';');
	}
	
	public void writeWithoutSemicolon(StringifyOutputStream out, ClassInfo classinfo) {
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(), classinfo).printsp(descriptor.type, classinfo);
		
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
		
		Modifiers modifiers = this.modifiers;
		
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