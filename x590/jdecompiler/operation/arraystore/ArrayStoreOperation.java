package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.IncrementableOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.operation.constant.IConstOperation;
import x590.jdecompiler.operation.anew.NewArrayOperation;
import x590.jdecompiler.operation.arrayload.ArrayLoadOperation;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public abstract class ArrayStoreOperation extends ReturnableOperation implements IncrementableOperation {
	
	private final Operation array, index, value;
	private final IncrementData incData;
	
	public ArrayStoreOperation(ArrayType requiredType, DecompilationContext context) {
		super(PrimitiveType.VOID);
		this.value = context.pop();
		this.index = context.popAsNarrowest(PrimitiveType.INT);
		this.array = context.pop();
		
		Type elementType = array.getReturnTypeAsNarrowest(requiredType).getElementType();
		
		value.castReturnTypeToNarrowest(elementType);
		
		if(array instanceof NewArrayOperation newArray && index instanceof IConstOperation iconst
				&& newArray.addToInitializer(this, iconst)) {
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
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		IncrementableOperation.super.writeTo(out, context, returnType, incData);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, array, context, Associativity.LEFT).print('[').print(index, context).print(']');
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.write(value, context);
	}
	
	@Override
	public void writeAsArrayInitializer(StringifyOutputStream out, StringifyContext context) {
		value.writeAsArrayInitializer(out, context);
	}
	
	
	@Override
	public boolean isLoadOperation(Operation operation) {
		return operation instanceof ArrayLoadOperation arrayLoad &&
				arrayLoad.getArray().equals(array) && arrayLoad.getIndex().equals(index);
	}
	
	@Override
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	
	@Override
	public boolean requiresLocalContext() {
		return !this.isRemoved() && (array.requiresLocalContext() || index.requiresLocalContext() || value.requiresLocalContext());
	}
}
