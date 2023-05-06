package x590.jdecompiler.operation.field;

import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.IncrementableOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;

public abstract class PutFieldOperation extends FieldOperation implements IncrementableOperation {
	
	private final Operation value;
	private Type returnType = PrimitiveType.VOID;
	private IncrementData incData;
	
	private Operation getValue(DecompilationContext context) {
		Operation value = context.popAsNarrowest(descriptor.getType());
		value.addPossibleVariableName(descriptor.getName());
		value.allowImplicitCast();
		return value;
	}
	
	public PutFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.value = getValue(context);
	}
	
	public PutFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.value = getValue(context);
	}
	
	public Operation getValue() {
		return value;
	}
	
	// Мы должны вызвать этот код только после popObject, поэтому он вызывается в дочернем инициализаторе
	protected void initIncData(DecompilationContext context) {
		this.incData = IncrementableOperation.super.init(context, value, descriptor.getType());
	}
	
	
	@Override
	public boolean isLoadOperation(Operation operation) {
		return operation instanceof GetFieldOperation getFieldOperation && getFieldOperation.getDescriptor().equals(descriptor);
	}
	
	@Override
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeTo(out, context, returnType, incData);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.print(value, context);
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return value.requiresLocalContext();
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof PutFieldOperation operation &&
				super.equals(operation) && value.equals(operation.value);
	}
}
