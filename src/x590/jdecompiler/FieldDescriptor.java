package x590.jdecompiler;

import java.lang.reflect.Field;

import x590.jdecompiler.attribute.AttributeNames;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.constpool.NameAndTypeConstant;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;

public final class FieldDescriptor extends Descriptor implements Importable {
	
	private final Type type;
	
	public FieldDescriptor(ReferenceType declaringClass, String name, Type type) {
		super(declaringClass, name);
		this.type = type;
	}
	
	public FieldDescriptor(ReferenceType declaringClass, String name, String descriptor) {
		this(declaringClass, name, Type.parseType(descriptor));
	}
	
	public FieldDescriptor(FieldrefConstant fieldref) {
		this(fieldref.getClassConstant().toClassType(), fieldref.getNameAndType());
	}
	
	public FieldDescriptor(ReferenceType declaringClass, NameAndTypeConstant nameAndType) {
		this(declaringClass, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public FieldDescriptor(ReferenceType declaringClass, ExtendedDataInputStream in, ConstantPool pool) {
		this(declaringClass, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	public static FieldDescriptor fromReflectField(ReferenceType declaringClass, Field field) {
		return new FieldDescriptor(declaringClass, field.getName(), Type.fromClass(field.getType()));
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
		return this == other || this.equals(other.getDeclaringClass(), other.getName(), other.type);
	}
	
	public boolean equals(ReferenceType declaringClass, String name, Type type) {
		return this.getDeclaringClass().equals(declaringClass) && this.getName().equals(name) && this.type.equals(type);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof FieldDescriptor other && this.equals(other);
	}
	
	public void writeType(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		FieldSignatureAttribute signature = attributes.getNullable(AttributeNames.SIGNATURE);
		
		if(signature != null) {
			signature.checkType(this);
		}
		
		out.writesp(signature != null ? signature.type : getType(), classinfo);
	}
}
