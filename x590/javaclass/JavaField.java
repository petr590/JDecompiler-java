package x590.javaclass;

import java.util.ArrayList;
import java.util.List;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.ConstantValueAttribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.IllegalModifiersException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.util.WhitespaceStringBuilder;
import x590.jdecompiler.JDecompiler;

import static x590.javaclass.Modifiers.*;

public class JavaField extends JavaClassMember {
	
	public final FieldDescriptor descriptor;
	public final Attributes attributes;
	
	public final ConstantValueAttribute constantValueAttribute;
//	public final Type genericType; // TODO
	
	private Operation initializer;
	
	protected JavaField(ExtendedDataInputStream in, ClassInfo classinfo, ConstantPool pool) {
		this(in.readUnsignedShort(), new FieldDescriptor(classinfo.thisType, in, pool), new Attributes(in, pool));
	}
	
	protected JavaField(int modifiers, FieldDescriptor descriptor, Attributes attributes) {
		super(modifiers);
		this.descriptor = descriptor;
		this.attributes = attributes;
		this.constantValueAttribute = attributes.get("ConstantValue");
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
		return super.canStringify(classinfo); // TODO
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		
		writeAnnotations(out, classinfo, attributes);
		
		out.printIndent().print(modifiersToString(), classinfo).print(descriptor, classinfo);
		
		if(initializer != null) {
			out.print(" = ");
			
			if(descriptor.type.isArrayType() && JDecompiler.getInstance().shortArrayInitAllowed())
				initializer.writeAsArrayInitializer(out, classinfo.getStaticInitializerStringifyContext());
			else
				initializer.writeTo(out, classinfo.getStaticInitializerStringifyContext());
		}
		
		out.println(';');
	}
	
	private WhitespaceStringBuilder modifiersToString() {
		WhitespaceStringBuilder str = new WhitespaceStringBuilder().printTrailingSpace();
		
		int modifiers = this.modifiers;
		
		switch(modifiers & ACC_ACCESS_FLAGS) {
			case ACC_VISIBLE   -> {}
			case ACC_PUBLIC    -> str.append("public");
			case ACC_PRIVATE   -> str.append("private");
			case ACC_PROTECTED -> str.append("protected");
			default ->
				throw new IllegalModifiersException(modifiers);
		}
		
		if((modifiers & ACC_STATIC) != 0)    str.append("static");
		if((modifiers & ACC_FINAL) != 0 && (modifiers & ACC_VOLATILE) != 0) throw new IllegalModifiersException(modifiers);
		if((modifiers & ACC_FINAL) != 0)     str.append("final");
		if((modifiers & ACC_TRANSIENT) != 0) str.append("transient");
		if((modifiers & ACC_VOLATILE) != 0)  str.append("volatile");
		
		return str;
	}
}