package x590.jdecompiler.constpool;

import java.io.DataOutputStream;
import java.io.IOException;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.MethodHandleConstOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public class MethodHandleConstant extends ConstValueConstant {
	
	public class ReferenceKind {
		
		private ReferenceKind() {}
		
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
	
	@Override
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
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof MethodHandleConstant constant && this.equals(constant);
	}
	
	public boolean equals(MethodHandleConstant other) {
		return this == other || referenceKind == other.referenceKind && reference.equals(other.reference);
	}
}
