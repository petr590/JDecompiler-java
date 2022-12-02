package x590.javaclass.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.operation.MethodHandleConstOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class MethodHandleConstant extends ConstValueConstant {
	
	public class ReferenceKind {
		
		public static final int
				GETFIELD = 1,
				GETSTATIC = 2,
				PUTFIELD = 3,
				PUTSTATIC = 4,
				INVOKEVIRTUAL = 5,
				INVOKESTATIC = 6,
				INVOKESPECIAL = 7,
				NEWINVOKESPECIAL = 8,
				INVOKEINTERFACE = 9;
	}
	
	public final int referenceKind;
	public final int referenceIndex;
	private ReferenceConstant reference;
	
	protected MethodHandleConstant(ExtendedDataInputStream in) {
		referenceKind = in.readByte();
		referenceIndex = in.readUnsignedShort();
	}
	
	@Override
	void init(ConstantPool pool) {
		reference = pool.get(referenceIndex);
	}
	
	public ReferenceConstant getReferenceConstant() {
		return reference;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "#MethodHandle#";
	}
	
	@Override
	public Type getType() {
		return ClassType.METHOD_HANDLE;
	}
	
	public String getConstantName() {
		return "MethodHandle";
	}
	
	@Override
	public Operation toOperation() {
		return new MethodHandleConstOperation(this);
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeByte(15);
		out.writeByte(referenceKind);
		out.writeShort(referenceIndex);
	}
}