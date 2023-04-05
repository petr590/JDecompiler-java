package x590.jdecompiler.field;

import java.lang.reflect.Field;

import x590.jdecompiler.Descriptor;
import x590.jdecompiler.Importable;
import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.constpool.NameAndTypeConstant;
import x590.jdecompiler.io.DisassemblingOutputStream;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;

public final class FieldDescriptor extends Descriptor implements Importable {
	
	private final Type type;
	
	public FieldDescriptor(FieldrefConstant fieldref) {
		this(fieldref.getClassConstant().toClassType(), fieldref.getNameAndType());
	}
	
	public FieldDescriptor(ReferenceType declaringClass, NameAndTypeConstant nameAndType) {
		this(declaringClass, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public FieldDescriptor(ReferenceType declaringClass, ExtendedDataInputStream in, ConstantPool pool) {
		this(declaringClass, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	public FieldDescriptor(ReferenceType declaringClass, String name, String descriptor) {
		this(Type.parseType(descriptor), declaringClass, name);
	}
	
	public FieldDescriptor(Type type, ReferenceType declaringClass, String name) {
		super(declaringClass, name);
		this.type = type;
	}
	
	public static FieldDescriptor fromReflectField(ReferenceType declaringClass, Field field) {
		return new FieldDescriptor(Type.fromClass(field.getType()), declaringClass, field.getName());
	}
	
	
	public Type getType() {
		return type;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
	}
	
	
	@Override
	public String toString() {
		return type.getName() + " " + this.getName();
	}
	
	public boolean equals(FieldDescriptor other) {
		return this == other || this.equals(other.type, other.getDeclaringClass(), other.getName());
	}
	
	public boolean equals(Type type, ReferenceType declaringClass, String name) {
		return this.type.equals(type) && getDeclaringClass().equals(declaringClass) && getName().equals(name);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof FieldDescriptor other && this.equals(other);
	}
	
	public void writeType(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		FieldSignatureAttribute signature = attributes.getNullable(AttributeType.FIELD_SIGNATURE);
		
		if(signature != null) {
			signature.checkType(this);
		}
		
		(signature != null ? signature.type : getType()).writeLeftDefinition(out, classinfo);
		out.printsp();
	}
	
	@Override
	public void writeDisassembled(DisassemblingOutputStream out, ClassInfo classinfo) {
		out.print(getName()).print(getType(), classinfo);
	}
}
