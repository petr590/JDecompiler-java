package x590.jdecompiler.attribute.signature;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ReferenceType;

public final class FieldSignatureAttribute extends SignatureAttribute {
	
	public final ReferenceType type;
	
	public FieldSignatureAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		ExtendedStringInputStream signatureIn = new ExtendedStringInputStream(pool.getUtf8String(in.readUnsignedShort()));
		this.type = Type.parseSignatureParameter(signatureIn);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		type.addImports(classinfo);
	}
	
	
	public void checkType(FieldDescriptor descriptor) {
		if(!type.equalsIgnoreSignature(descriptor.getType()))
			throw new DecompilationException("Field signature doesn't matches the field type: " + type + " and " + descriptor.getType());
	}
	
	public FieldDescriptor createGenericDescriptor(ClassInfo classinfo, FieldDescriptor descriptor) {
		return FieldDescriptor.of(type, descriptor.getDeclaringClass(), descriptor.getName());
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof FieldSignatureAttribute signature && this.equals(signature);
	}
	
	public boolean equals(FieldSignatureAttribute other) {
		return this == other || this.type.equals(other.type);
	}
}
