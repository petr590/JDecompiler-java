package x590.jdecompiler;

import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.attribute.signature.FieldSignatureAttribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.constpool.NameAndTypeConstant;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public final class FieldDescriptor extends Descriptor implements Importable {
	
	private final Type type;
	
	public FieldDescriptor(ClassType clazz, String name, Type type) {
		super(clazz, name);
		this.type = type;
	}
	
	public FieldDescriptor(ClassType clazz, String name, String descriptor) {
		this(clazz, name, Type.parseType(descriptor));
	}
	
	public FieldDescriptor(FieldrefConstant fieldref) {
		this(fieldref.getClassConstant().toClassType(), fieldref.getNameAndType());
	}
	
	public FieldDescriptor(ClassType clazz, NameAndTypeConstant nameAndType) {
		this(clazz, nameAndType.getNameConstant().getString(), nameAndType.getDescriptor().getString());
	}
	
	public FieldDescriptor(ClassType clazz, ExtendedDataInputStream in, ConstantPool pool) {
		this(clazz, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
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
		return this == other || (this.getName().equals(other.getName()) && type.equals(other.type));
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof FieldDescriptor other && this.equals(other);
	}
	
	public void writeType(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		FieldSignatureAttribute signature = attributes.get("Signature");
		
		if(signature != null) {
			signature.checkType(this);
		}
		
		out.writesp(signature != null ? signature.type : getType(), classinfo);
	}
}
