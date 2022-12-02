package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.ClassConstOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.ReferenceType;
import x590.javaclass.type.Type;

public class ClassConstant extends ConstValueConstant {

	public final int nameIndex;
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

	@Override
	void init(ConstantPool pool) {
		name = pool.get(nameIndex);
	}
	
	public Utf8Constant getName() {
		return name;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return Type.parseReferenceType(name.getValue()).toString(classinfo) + ".class";
	}
	
	@Override
	public Type getType() {
		return ClassType.CLASS;
	}
	
	
	public ReferenceType toReferenceType() {
		if(referenceType != null)
			return referenceType;
		
		return referenceType = Type.parseReferenceType(name.getValue());
	}
	
	public ClassType toClassType() {
		if(classType != null)
			return classType;
		
		referenceType = classType = ClassType.valueOf(name.getValue());
		
		return classType;
	}
	
	public ArrayType toArrayType() {
		if(arrayType != null)
			return arrayType;
		
		referenceType = arrayType = new ArrayType(name.getValue());
		
		return arrayType;
	}
	
	
	public String getConstantName() {
		return "Class";
	}
	
	public Operation toOperation() {
		return new ClassConstOperation(this);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(7);
		out.writeShort(nameIndex);
	}
}