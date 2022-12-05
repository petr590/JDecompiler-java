package x590.javaclass;

import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.constpool.NameAndTypeConstant;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class FieldDescriptor extends Descriptor implements StringWritableAndImportable {
	
	public final ClassType clazz;
	public final String name;
	public final Type type;
	
	public FieldDescriptor(ClassType clazz, String name, Type type) {
		this.clazz = clazz;
		this.name = name;
		this.type = type;
	}
	
	public FieldDescriptor(ClassType clazz, String name, String descriptor) {
		this(clazz, name, Type.parseType(descriptor));
	}
	
	public FieldDescriptor(FieldrefConstant fieldref) {
		this(fieldref.getClassConstant().toClassType(), fieldref.getNameAndType());
	}
	
	public FieldDescriptor(ClassType clazz, NameAndTypeConstant nameAndType) {
		this(clazz, nameAndType.getName().getValue(), nameAndType.getDescriptor().getValue());
	}
	
	public FieldDescriptor(ClassType clazz, ExtendedDataInputStream in, ConstantPool pool) {
		this(clazz, pool.getUtf8String(in.readUnsignedShort()), pool.getUtf8String(in.readUnsignedShort()));
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportIfReferenceType(type);
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
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(type, classinfo).printsp().print(name);
	}
}