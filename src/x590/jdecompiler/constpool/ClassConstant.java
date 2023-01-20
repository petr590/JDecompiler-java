package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.constant.ClassConstOperation;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;

public final class ClassConstant extends ConstValueConstant {
	
	private final int nameIndex;
	private Utf8Constant name;
	private ReferenceType referenceType;
	private ClassType classType;
	private ArrayType arrayType;
	
	protected ClassConstant(ExtendedDataInputStream in) {
		this.nameIndex = in.readUnsignedShort();
	}
	
	public ClassConstant(int nameIndex, ConstantPool pool) {
		this.nameIndex = nameIndex;
		init(pool);
	}
	
	
	public int getNameIndex() {
		return nameIndex;
	}
	
	
	@Override
	void init(ConstantPool pool) {
		name = pool.get(nameIndex);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(this.toReferenceType());
	}
	
	
	public Utf8Constant getNameConstant() {
		return name;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return this.toReferenceType().toString(classinfo) + ".class";
	}
	
	
	@Override
	public Type getType() {
		return ClassType.CLASS;
	}
	
	public ReferenceType toReferenceType() {
		if(referenceType != null)
			return referenceType;
		
		referenceType = Type.parseReferenceType(name.getString());
		
		if(referenceType.isBasicClassType())
			classType = (ClassType)referenceType;
		else if(referenceType.isBasicArrayType())
			arrayType = (ArrayType)referenceType;
		
		return referenceType;
	}
	
	public ClassType toClassType() {
		if(classType != null)
			return classType;
		
		referenceType = classType = ClassType.fromDescriptor(name.getString());
		
		return classType;
	}
	
	public ArrayType toArrayType() {
		if(arrayType != null)
			return arrayType;
		
		referenceType = arrayType = ArrayType.fromDescriptor(name.getString());
		
		return arrayType;
	}
	
	@Override
	public String getConstantName() {
		return "Class";
	}
	
	@Override
	public Operation toOperation() {
		return new ClassConstOperation(this);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(7);
		out.writeShort(nameIndex);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof ClassConstant constant && this.equals(constant);
	}
	
	public boolean equals(ClassConstant other) {
		return this == other || this.name.equals(other.name);
	}
}
