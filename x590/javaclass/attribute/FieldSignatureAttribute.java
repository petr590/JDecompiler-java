package x590.javaclass.attribute;

import x590.javaclass.FieldDescriptor;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.type.ReferenceType;
import x590.javaclass.type.Type;

public class FieldSignatureAttribute extends SignatureAttribute {
	
	public final ReferenceType type;
	
	protected FieldSignatureAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		ExtendedStringReader signatureIn = new ExtendedStringReader(pool.getUtf8String(in.readUnsignedShort()));
		this.type = Type.parseSignatureParameter(signatureIn);
	}
	
	public void checkType(FieldDescriptor descriptor) {
		if(!type.baseEquals(descriptor.type))
			throw new DecompilationException("Field signature doesn't matches the field type: " + type + " and " + descriptor.type);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof FieldSignatureAttribute signature && this.equals(signature);
	}
	
	public boolean equals(FieldSignatureAttribute other) {
		return this == other || this.type.equals(other.type);
	}
}
