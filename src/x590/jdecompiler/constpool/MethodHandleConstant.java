package x590.jdecompiler.constpool;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.MethodHandleConstOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;

public final class MethodHandleConstant extends ConstValueConstant {
	
	public enum ReferenceKind {
		GETFIELD  (1),
		GETSTATIC (2),
		PUTFIELD  (3),
		PUTSTATIC (4),
		INVOKEVIRTUAL    (5, 1),
		INVOKESTATIC     (6, 0),
		INVOKESPECIAL    (7, 1),
		NEWINVOKESPECIAL (8, 0),
		INVOKEINTERFACE  (9, 1);
		
		private static final ReferenceKind[] VALUES = values();
		
		private final int index;
		private final int argumentsForLambdaReference;
		private final String name;
		
		private ReferenceKind(int index) {
			this(index, -1);
		}
		
		private ReferenceKind(int index, int argumentsForLambdaReference) {
			this.index = index;
			this.argumentsForLambdaReference = argumentsForLambdaReference;
			this.name = name().toLowerCase();
		}
		
		public int getIndex() {
			return index;
		}
		
		public int argumentsForLambdaReference() {
			return argumentsForLambdaReference;
		}
		
		public static ReferenceKind byIndex(int index) {
			if(index > 0 && index <= VALUES.length) {
				return VALUES[index - 1];
			}
			
			throw new DisassemblingException("Invalid referenceKind: " + index);
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private final ReferenceKind referenceKind;
	private final int referenceIndex;
	private ReferenceConstant reference;
	
	protected MethodHandleConstant(ExtendedDataInputStream in) {
		referenceKind = ReferenceKind.byIndex(in.readByte());
		referenceIndex = in.readUnsignedShort();
	}
	
	@Override
	protected void init(ConstantPool pool) {
		reference = pool.get(referenceIndex);
	}
	
	public ReferenceKind getReferenceKind() {
		return referenceKind;
	}
	
	public ReferenceConstant getReferenceConstant() {
		return reference;
	}
	
	public FieldrefConstant getFieldrefConstant() {
		if(reference instanceof FieldrefConstant fieldref)
			return fieldref;
		
		throw new DisassemblingException("Expected Fieldref, got " + reference.getConstantName());
	}
	
	public MethodrefConstant getMethodrefConstant() {
		if(reference instanceof MethodrefConstant methodref)
			return methodref;
		
		throw new DisassemblingException("Expected Methodref, got " + reference.getConstantName());
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
	public String toString() {
		return String.format("MethodHandleConstant { referenceKind = %s, reference = %s }", referenceKind, reference);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print("#MethodHandle#");
	}
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		out.writeByte(TAG_METHOD_HANDLE);
		out.writeByte(referenceKind.getIndex());
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
