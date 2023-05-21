package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.operation.array.NewArrayOperation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.operation.increment.IncrementableOperation;
import x590.jdecompiler.operation.arrayload.ArrayLoadOperation;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.IArrayType;

public abstract class ArrayStoreOperation extends ReturnableOperation implements IncrementableOperation {
	
	private final Operation array, index, value;
	private final IncrementData incData;
	
	
	private void castArrayType(Type arrayType) {
		array.castReturnTypeToWidest(ArrayType.forType(
				value.getReturnTypeAsNarrowest(((IArrayType)arrayType).getElementType())
		));
	}
	
	
	public ArrayStoreOperation(ArrayType requiredType, DecompilationContext context) {
		super(PrimitiveType.VOID);
		this.value = context.pop();
		this.index = context.popAsNarrowest(PrimitiveType.INT);
		this.array = context.pop();
		
		castArrayType(array.getReturnTypeAsNarrowest(requiredType));
		
		if(array instanceof NewArrayOperation newArray &&
			index instanceof IConstOperation iconst &&
			newArray.addToInitializer(value, iconst)) {
			
			this.remove();
		}
		
		this.incData = init(context, value, value.getReturnType());
	}
	
	public Operation getArray() {
		return array;
	}
	
	public Operation getIndex() {
		return index;
	}
	
	public Operation getValue() {
		return value;
	}
	
	
	@Override
	protected void onCastReturnType(Type newType, CastingKind kind) {
		super.onCastReturnType(newType, kind);
		array.castReturnTypeTo(ArrayType.forType(newType), kind);
	}
	
	@Override
	protected Type getDeducedType(Type returnType) {
		castArrayType(array.getReturnType());
		
		var preIncLoadOperation = incData.getPreIncLoadOperation();
		
		return preIncLoadOperation != null ? 
				preIncLoadOperation.getReturnType() :
				returnType;
	}
	
	@Override
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		IncrementableOperation.super.writeTo(out, context, returnType, incData);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, array, context, Associativity.LEFT).print('[').print(index, context).print(']');
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.print(value, context);
	}
	
	@Override
	public void allowShortArrayInitializer() {
		value.allowShortArrayInitializer();
	}
	
	
	@Override
	public boolean isLoadOperation(Operation operation) {
		return operation instanceof ArrayLoadOperation arrayLoad &&
				arrayLoad.getArray().equals(array) && arrayLoad.getIndex().equals(index);
	}
	
	
	@Override
	public boolean requiresLocalContext() {
		return !this.isRemoved() && (array.requiresLocalContext() || index.requiresLocalContext() || value.requiresLocalContext());
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof ArrayStoreOperation operation &&
				array.equals(operation.array) && index.equals(operation.index) && value.equals(operation.value);
	}
}
