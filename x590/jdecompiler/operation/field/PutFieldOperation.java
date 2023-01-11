package x590.jdecompiler.operation.field;

import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.IncrementableOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;

public abstract class PutFieldOperation extends FieldOperation implements IncrementableOperation {
	
	protected final Operation value;
	
	protected Type returnType;
	
	protected IncrementData incData;
	
	private Operation getValue(DecompilationContext context) {
		Operation value = context.stack.popAsNarrowest(descriptor.type);
		value.addVariableName(descriptor.name);
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
	
	// Мы должны вызвать этот код только после popObject, поэтому он вызывается в дочернем инициализаторе
	public void initIncData(DecompilationContext context) {
		this.incData = IncrementableOperation.super.init(context, value, descriptor.type);
	}
	
	
	@Override
	public boolean canIncrement() {
		return descriptor.type.isPrimitive();
	}
	
	@Override
	public boolean isLoadOperation(Operation operation) {
		return operation instanceof GetFieldOperation getFieldOperation && getFieldOperation.descriptor.equals(descriptor);
	}
	
	@Override
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	@Override
	public Type getReturnTypeFor(Operation operation) {
		return ((GetFieldOperation)operation).descriptor.type;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeTo(out, context, returnType, incData);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		out.write(descriptor.name);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.write(value, context);
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return value.requiresLocalContext();
	}
}
