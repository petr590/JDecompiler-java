package x590.javaclass;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.attribute.FieldSignatureAttribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.constpool.NameAndTypeConstant;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class FieldDescriptor extends Descriptor implements Importable {
	
	public final Type type;
	
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
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
	}
	
	
	public String toString() {
		return type.getName() + " " + name;
	}
	
	public boolean equals(FieldDescriptor other) {
		return this == other || (this.name.equals(other.name) && this.type.equals(other.type));
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
		
		out.writesp(signature != null ? signature.type : type, classinfo);
	}
}
